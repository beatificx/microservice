package org.beatific.microservice.container.service.event;

import java.util.EventObject;

import org.beatific.microservice.container.context.ServiceContext;

public class ServiceEvent extends EventObject {

	private static final long serialVersionUID = 448885504351905773L;

	public ServiceEvent(Object source) {
		super(source);
	}
	
	public ServiceContext getServiceContext() {
		Object source = getSource();
		if(source instanceof ServiceContext)return (ServiceContext)source;
		else return null;
	}

}
