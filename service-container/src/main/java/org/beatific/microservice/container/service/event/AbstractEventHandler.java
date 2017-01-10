package org.beatific.microservice.container.service.event;

import java.lang.reflect.ParameterizedType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractEventHandler<T extends ServiceEvent> implements EventHandler<T> {

	private String genericTypeName;
	
	public AbstractEventHandler() {
		genericTypeName = getGenericName();
	}
	
	protected abstract void handleMyEvent(T event);
	
	@SuppressWarnings("unchecked")
	@Override
	public void handle(ServiceEvent event) {
		log.debug("MyEvent[" + getGenericName() + "], GivenEvent[" + event.getClass().getName() + "]");
		if(event.getClass().getName().equals(genericTypeName)) handleMyEvent((T)event);
	}
	
	@SuppressWarnings("unchecked")
	private String getGenericName()
	{
	    return ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getName();
	}

}
