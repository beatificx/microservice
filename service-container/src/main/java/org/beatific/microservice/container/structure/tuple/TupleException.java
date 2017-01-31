package org.beatific.microservice.container.structure.tuple;

public class TupleException extends RuntimeException{

	private static final long serialVersionUID = 6578402916257572579L;
	
	public TupleException() { super(); };
	public TupleException(Throwable t) { super(t); };
	public TupleException(String m) { super(m); };
	public TupleException(String m, Throwable t) { super(m, t); };

}
