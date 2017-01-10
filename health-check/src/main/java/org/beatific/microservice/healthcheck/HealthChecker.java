package org.beatific.microservice.healthcheck;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class HealthChecker implements HealthIndicator {

	@Override
	public Health health() {
		boolean isOk = check();
		if (!isOk) {
			return Health.down().withDetail("Error Code", 10000).build();
		}
		return Health.up().build();
	}

	public boolean check() {
		return true;
	}

}