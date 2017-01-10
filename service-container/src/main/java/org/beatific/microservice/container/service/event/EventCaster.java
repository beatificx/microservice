package org.beatific.microservice.container.service.event;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventCaster {

	@Autowired(required=false)
	private AbstractEventHandler<?>[] handlerArray;
	private List<AbstractEventHandler<?>> handlers;
	
	@PostConstruct
	public void init() {
		if(handlerArray != null)handlers = Arrays.asList(handlerArray);
	}
	
	public void registerHandler(AbstractEventHandler<?> handler) {
		this.handlers.add(handler);
	}
	
	public void cast(ServiceEvent event) {
		for(AbstractEventHandler<? extends ServiceEvent> handler : this.handlers) {
			handler.handle(event);
		}
	}
	
	
}
