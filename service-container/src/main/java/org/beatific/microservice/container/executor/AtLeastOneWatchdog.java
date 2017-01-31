package org.beatific.microservice.container.executor;

import org.beatific.microservice.container.instance.InstanceFinder;
import org.beatific.microservice.container.instance.InstancePhysicalManager;
import org.beatific.microservice.container.service.ServicePool;
import org.beatific.microservice.container.utils.CopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AtLeastOneWatchdog implements Watchdog {

	@Autowired
	InstancePhysicalManager physicalManager;
	
	@Autowired
	ServicePool servicePool;
	
	@Autowired
	InstanceFinder finder;
	
	@Override
	public void watch() {
		
		log.debug("services {}", servicePool.services());
		
		CopyUtils.copy(servicePool.services()).stream().parallel().filter(s -> finder.getAllInstaces(s.getServiceName()).size() == 0).forEach(s -> physicalManager.invoke(s));
		//parallel의 경우 filter앞에 써도 foreach까지 적용되는지 확인이 필요함 / 향후 두개 이상의 서비스 등록으로 테스트 필요
		
	}
}
