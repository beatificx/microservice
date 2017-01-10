package org.beatific.microservice.container.executor;

import java.io.IOException;

public class DreddExecutorTest {

//	@Test
	public void testExeucte() {
		Executor executor = new DreddExecutor();
		try {
			executor.execute("java -jar C:/Users/Administrator/git/microservice/point/target/point-service-0.0.1-SNAPSHOT.jar");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
