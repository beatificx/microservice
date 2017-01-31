package org.beatific.microservice.container.structure.match;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class BiMatch<T, S> {
	
	public <U> CaseOf<U> caseOf(BiPredicate<T, S> cond, BiFunction<T, S, U> map) {
		return this.new CaseOf<U>(cond, map, Optional.empty());
	}

	public class CaseOf<U> implements BiFunction<T, S, Optional<U>> {
		private BiPredicate<T, S> cond;
		private BiFunction<T, S, U> map;
		private Optional<CaseOf<U>> previous;

		CaseOf(BiPredicate<T, S> cond, BiFunction<T, S, U> map, Optional<CaseOf<U>> previous) {
			this.cond = cond;
			this.map = map;
			this.previous = previous;
		}

		@Override
		public Optional<U> apply(T v1, S v2) {
			Optional<U> r = previous.flatMap(p -> p.apply(v1, v2));
			return r.isPresent() || !cond.test(v1, v2) ? r : Optional.of(this.map.apply(v1, v2));
		}

		public CaseOf<U> caseOf(BiPredicate<T, S> cond, BiFunction<T, S, U> map) {
			return new CaseOf<U>(cond, map, Optional.of(this));
		}

		public BiFunction<T, S, U> otherwise(BiFunction<T, S, U> map) {
			return (v1, v2) -> this.apply(v1, v2).orElseGet(() -> map.apply(v1, v2));
		}
	}
	
}