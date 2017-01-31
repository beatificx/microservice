package org.beatific.microservice.container.monitor;

public class MonitorException extends RuntimeException {

	private static final long serialVersionUID = -7905934320416131763L;

	public MonitorException() {
		super();
	}
	
	public MonitorException(Throwable t) {
		super(t);
	}
	
	public MonitorException(String m) {
		super(m);
	}
	
	public MonitorException(String m, Throwable t) {
		super(m, t);
	}
}
