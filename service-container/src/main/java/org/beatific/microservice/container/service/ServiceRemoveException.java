package org.beatific.microservice.container.service;

public class ServiceRemoveException extends Exception {

	private static final long serialVersionUID = -1953441000087566567L;

	public ServiceRemoveException(String m) {
		super(m);
	}
	
	public ServiceRemoveException(Throwable t) {
		super(t);
	}
	
	public ServiceRemoveException(String m, Throwable t) {
		super(m, t);
	}
}
