package org.beatific.microservice.container.service;

public class ServiceRegistrationException extends Exception {

	private static final long serialVersionUID = -1953441000087566567L;

	public ServiceRegistrationException(String m) {
		super(m);
	}
	
	public ServiceRegistrationException(Throwable t) {
		super(t);
	}
	
	public ServiceRegistrationException(String m, Throwable t) {
		super(m, t);
	}
}
