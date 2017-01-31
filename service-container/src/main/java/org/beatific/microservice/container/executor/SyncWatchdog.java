package org.beatific.microservice.container.executor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.beatific.microservice.container.instance.InstanceLogicalManager;
import org.beatific.microservice.container.instance.InstancePhysicalManager;
import org.beatific.microservice.container.instance.InstancePool;
import org.beatific.microservice.container.repository.RepositoryException;
import org.beatific.microservice.container.utils.CopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SyncWatchdog {

	@Autowired
	private InstanceLogicalManager manager;

	@Autowired
	private InstancePhysicalManager physicalManager;

	@Autowired
	private InstancePool instancePool;


	public void watch() {
		
		log.debug("instances [{}]", instancePool.instances());
		
		CopyUtils.copy(instancePool.instances()).stream()
		         .filter(i ->  { try{ return i.getPid() > 0 && !physicalManager.isProcessRunning(i.getPid(), 60, TimeUnit.SECONDS); } catch (IOException e) {return false;}})
		         .collect(Collectors.toList()).forEach(i -> { try{ manager.unregister(i); instancePool.remove(i);} catch (RepositoryException e) {e.printStackTrace();}});
	}

}
