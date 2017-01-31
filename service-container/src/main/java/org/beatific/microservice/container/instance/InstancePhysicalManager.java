package org.beatific.microservice.container.instance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.OS;
import org.apache.commons.exec.PumpStreamHandler;
import org.beatific.microservice.container.executor.Executor;
import org.beatific.microservice.container.repository.RepositoryException;
import org.beatific.microservice.container.service.Service;
import org.beatific.microservice.container.service.ServicePool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InstancePhysicalManager {

	@Autowired
	Executor executor;

	@Autowired
	InstanceFinder finder;

	@Autowired
	private ServicePool servicePool;

	@Autowired
	private InstancePool instancePool;

	/**
	 * service 구동이 완료되면(discovery server 확인) return해야 함
	 * 
	 * @param serviceName
	 */
	public void invoke(String serviceName) {

		Service service = servicePool.get(serviceName);
		invoke(service);
	}
	
	public void invoke(Service service) {
		Instance instance = null;

		try {
			int pid = executor.execute(service.getStartCommand());

			while (instance == null) {

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				instance = finder.getAllInstaceInfos(service.getServiceName()).stream()
						.filter(ins -> !instancePool.contains(ins.getUri())).map(ins -> {
							ins.setService(service);
							ins.setPid(pid);
							return ins;
						}).findFirst().orElse(null);

				log.debug("Finded service name {}, instance {}", service.getServiceName(), instance);

			}

			instancePool.register(instance);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (RepositoryException e) {
			// kill instance
		}
	}

	public void shutdown(Instance instance) {

		int pid = instance.getPid();
		
		try {
			
			if(pid <= 0) instancePool.zombie(instance); 
			else {
				if (OS.isFamilyWindows()) {
					executor.execute("cmd /c tasklist /PID " + pid );
				} else {
				    executor.execute("kill -9 " + pid);
				}
				
				if(isProcessRunning(pid, 30, TimeUnit.SECONDS)) {
					instancePool.zombie(instance);
				}
			}
			
			instancePool.remove(instance);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isProcessRunning(int pid, int timeout, TimeUnit timeunit) throws IOException {
		String line;
		if (OS.isFamilyWindows()) {
			// tasklist exit code is always 0. Parse output
			// findstr exit code 0 if found pid, 1 if it doesn't
			line = "cmd /c \"tasklist /FI \"PID eq " + pid + "\" | findstr " + pid + "\"";
		} else {
			// ps exit code 0 if process exists, 1 if it doesn't
			line = "ps -p " + pid;
			// `-p` is POSIX/BSD-compliant, `--pid`
			// isn't<ref>https://github.com/apache/storm/pull/296#discussion_r20535744</ref>
		}
		CommandLine cmdLine = CommandLine.parse(line);
		DefaultExecutor executor = new DefaultExecutor();
		// disable logging of stdout/strderr
		executor.setStreamHandler(new PumpStreamHandler(null, null, null));
		// disable exception for valid exit values
		executor.setExitValues(new int[] { 0, 1 });
		// set timer for zombie process
		ExecuteWatchdog timeoutWatchdog = new ExecuteWatchdog(timeunit.toMillis(timeout));
		executor.setWatchdog(timeoutWatchdog);
		int exitValue = executor.execute(cmdLine);
		// 0 is the default exit code which means the process exists
		return exitValue == 0;
	}

}
