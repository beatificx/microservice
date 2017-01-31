package org.beatific.microservice.container.structure.tuple;

public class Tuple2<X, Y> {

	private final X x;
	private final Y y;

	public Tuple2(X x, Y y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "Tuple2 [" + x + ", " + y + "]";
	}

	public X first() {
		return x;
	}

	public Y second() {
		return y;
	}

	public Object at(int index) {
		switch (index) {
		case 0:
			return x;
		case 1:
			return y;
		default:
			throw new TupleException("not Exists index : " + index);
		}
	}

//	Map<BiPredicate<X, Y>, BiFunction<X, Y, Z>> conditions; 
//
//	public Tuple2<X, Y, Z> caseOf(BiPredicate<X, Y> cond, BiFunction<X, Y, Z> f) {
//		conditions.put(cond, f);
//		return this;
//	}
//	
//	public Z elseThen(BiFunction<X, Y, Z> f) {
//		
//		for(Entry<BiPredicate<X, Y>, BiFunction<X, Y, Z>> entry : conditions.entrySet()) {
//			if(entry.getKey().test(x, y)) return entry.getValue().apply(x, y);
//		}
//		
//		return f.apply(x, y);
//	}
	
}
