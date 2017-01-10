package org.beatific.microservice.healthcheck;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import com.netflix.appinfo.HealthCheckHandler;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.InstanceInfo.InstanceStatus;
import com.netflix.discovery.CacheRefreshedEvent;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaEvent;
import com.netflix.discovery.EurekaEventListener;
import com.netflix.discovery.StatusChangeEvent;

public class EurekaRegistery implements EurekaEventListener {

	@Autowired
	private EurekaClient client;

	@PostConstruct
	public void regist() {
		client.registerEventListener(this);
		client.registerHealthCheck(paramHealthCheckHandler);
	}

	@Override
	public void onEvent(EurekaEvent event) {
		
		if (event instanceof StatusChangeEvent) {
			if (((StatusChangeEvent) event).getStatus().equals(InstanceInfo.InstanceStatus.UP)) {
				client.unregisterEventListener(this);
			}
		} else if(event instanceof CacheRefreshedEvent) {
			
		}
	}
	
	private class DefaultHealthCheckHandler implements HealthCheckHandler { 

        private final HealthIndicatorService healthIndicatorService; 

        public DefaultHealthCheckHandler( 
                        final HealthIndicatorService healthIndicatorService) { 
                this.healthIndicatorService = healthIndicatorService; 
        }

        @Override 
        public InstanceStatus getStatus(final InstanceStatus currentStatus) { 

                InstanceStatus newStatus = InstanceStatus.UP; 

                final Health health = this.healthIndicatorService.health(); 
                if (!Status.UP.equals(health.getStatus())) { 
                        newStatus = InstanceStatus.OUT_OF_SERVICE; 
                } 

                return newStatus; 
        } 

} 
}


