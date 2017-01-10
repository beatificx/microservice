package org.beatific.microservice.container.service;

public class ServiceLoadException extends Exception {

	private static final long serialVersionUID = 3184160608157244684L;

	public ServiceLoadException(String m) {
		super(m);
	}
	
	public ServiceLoadException(Throwable t) {
		super(t);
	}
	
	public ServiceLoadException(String m, Throwable t) {
		super(m, t);
	}
}
