package org.beatific.microservice.container;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

//@EnableResourceServer
@SpringCloudApplication
public class ServiceContainerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceContainerApplication.class, args);
	}

	@Configuration
	@EnableScheduling
	public class AppConfig implements SchedulingConfigurer {

		@Override
		public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
			taskRegistrar.setScheduler(taskExecutor());
		}

		@Bean(destroyMethod = "shutdown")
		public Executor taskExecutor() {
			return Executors.newScheduledThreadPool(2);
		}
	}
}
