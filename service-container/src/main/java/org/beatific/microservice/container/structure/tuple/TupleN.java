package org.beatific.microservice.container.structure.tuple;

import java.util.Arrays;
import java.util.List;

public class TupleN {

	private final List<Object> elements;

	public TupleN(final Object... elements) {
		this.elements = Arrays.asList(elements);
	}

	@Override
	public String toString() {
		return elements.toString();
	}

	public Object at(final int index) {
		return elements.get(index);
	}

}
