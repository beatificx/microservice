package org.beatific.microservice.container.repository;

public class RepositoryException extends Exception {

	private static final long serialVersionUID = -1360288418730274838L;

	public RepositoryException() {
		super();
	}
	
    public RepositoryException(Throwable t) {
		super(t);
	}
    
    public RepositoryException(String m) {
		super(m);
	}
    
    public RepositoryException(String m, Throwable t) {
		super(m, t);
	}
}
