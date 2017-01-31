package org.beatific.microservice.container.executor;

import java.io.IOException;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DreddExecutor extends Executor {

	@Override
	public int execute(String command) throws IOException {

		log.info("command {}", command);
		Process process = launch(command);
		return getPid(process);
	}
	
	protected Process launch(String command) throws IOException {
		Process process = Runtime.getRuntime().exec(command);
		closeProcessStreams(process);

		return process;
	}

	private void closeProcessStreams(Process process) throws IOException {
		process.getInputStream().close();
		process.getOutputStream().close();
		process.getErrorStream().close();
	}

}
