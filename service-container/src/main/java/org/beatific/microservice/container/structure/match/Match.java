package org.beatific.microservice.container.structure.match;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class Match<T> {
	
	public <U> CaseOf<U> caseOf(Predicate<T> cond, Function<T, U> map) {
		return this.new CaseOf<U>(cond, map, Optional.empty());
	}

	class CaseOf<U> implements Function<T, Optional<U>> {
		private Predicate<T> cond;
		private Function<T, U> map;
		private Optional<CaseOf<U>> previous;

		CaseOf(Predicate<T> cond, Function<T, U> map, Optional<CaseOf<U>> previous) {
			this.cond = cond;
			this.map = map;
			this.previous = previous;
		}

		@Override
		public Optional<U> apply(T value) {
			Optional<U> r = previous.flatMap(p -> p.apply(value));
			return r.isPresent() || !cond.test(value) ? r : Optional.of(this.map.apply(value));
		}

		public CaseOf<U> caseOf(Predicate<T> cond, Function<T, U> map) {
			return new CaseOf<U>(cond, map, Optional.of(this));
		}

		public Function<T, U> otherwise(Function<T, U> map) {
			return value -> this.apply(value).orElseGet(() -> map.apply(value));
		}
	}
	
//	final class Test
//	{
//	    public static final Function<Integer, Integer> fact = new Match<Integer>()
//	            .caseOf( i -> i == 0, i -> 1 )
//	            .otherwise( i -> i * Test.fact.apply(i - 1) );
//
//	    public static final Function<Object, String> dummy = new Match<Object>()
//	            .caseOf( i -> i.equals(42), i -> "forty-two" )
//	            .caseOf( i -> i instanceof Integer, i -> "Integer : " + i.toString() )
//	            .caseOf( i -> i.equals("world"), i -> "Hello " + i.toString() )
//	            .otherwise( i -> "got this : " + i.toString() );
//
//	    public static void main(String[] args)
//	    {
//	        System.out.println("factorial : " + fact.apply(6));
//	        System.out.println("dummy : " + dummy.apply(42));
//	        System.out.println("dummy : " + dummy.apply(6));
//	        System.out.println("dummy : " + dummy.apply("world"));
//	        System.out.println("dummy : " + dummy.apply("does not match"));
//	    }
//	}
}