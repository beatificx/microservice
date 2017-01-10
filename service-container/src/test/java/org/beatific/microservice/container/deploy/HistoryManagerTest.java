package org.beatific.microservice.container.deploy;

import org.beatific.microservice.container.environment.Server;
import org.junit.Test;

public class HistoryManagerTest {
	
	@Test
	public void testWriteDownHistory() {
		HistoryManager manager = new HistoryManager();
		Deployment deployment = new Deployment();
		deployment.setServer(Server.Dev);
		deployment.setVersion("1.0.0.RELEASE");
		deployment.setServiceName("test");
		manager.writeDownHistory(deployment);
		System.out.println(manager.get());
	}
	
	@Test
	public void testLoad() {
		HistoryManager manager = new HistoryManager();
		manager.load();
		System.out.println(manager.get());
	}

}
