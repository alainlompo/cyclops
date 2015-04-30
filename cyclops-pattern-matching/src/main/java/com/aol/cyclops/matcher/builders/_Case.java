package com.aol.cyclops.matcher.builders;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import org.hamcrest.Matcher;
import org.jooq.lambda.Seq;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import lombok.experimental.Wither;

import com.aol.cyclops.matcher.PatternMatcher;
import com.aol.cyclops.matcher.PatternMatcher.ActionWithReturn;


/**
 * Case builder for Algebraic Data Type or Case class matching
 * 
 * @author johnmcclean
 *
 * @param <X> Return type from the Pattern Matching expression
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class _Case<X> extends Case {
	// T : user input (type provided to match)
	// X : match response (thenApply)
	// R : extractor response
	// V : input for matcher / predicate
	@Getter(AccessLevel.PACKAGE)
	@Wither(AccessLevel.PACKAGE)
	private final PatternMatcher patternMatcher;

	/**
	 * Build a Case where we will check if user input matches the Type of the input params on the ActionWithReturn instance supplied
	 * If it does, the ActionWithReturn will be executed (applied) to get the result of the Match.
	 * 
	 * @param a Action from which the Predicate (by param type) and Function will be extracted to build a Pattern Matching case
	 * @return Next step in Case builder
	 */
	public <T, R> AndMembersMatchBuilder<T, R> isType(ActionWithReturn<T, R> a) {

		return new AndMembersMatchBuilder<T, R>(a);

	}

	@AllArgsConstructor
	public class AndMembersMatchBuilder<T, R> {
		ActionWithReturn<T, R> action;

		/**
		 * 
		 * Provide a comparison value, JDK 8 Predicate, or Hamcrest Matcher  for each Element to match on.
		 * 
		 * Further & recursively unwrap any element by Predicates.type(ELEMENT_TYPE.class).with(V... values)
		 * 
		 * @see Predicates#type
		 * 
		 * @param values Matching rules for each element in the decomposed / unapplied user input
		 * @return Pattern Matcher builder with completed Case added to it
		 */
		@SafeVarargs
		public final <V> MatchingInstance<T, X> with(V... values) {
			val type = action.getType();
			val clazz = type.parameterType(type.parameterCount() - 1);
			Predicate predicate = it -> Optional.of(it)
					.map(v -> v.getClass().isAssignableFrom(clazz))
					.orElse(false);
			// add wildcard support
			Predicate<V>[] predicates = Seq.of(values)
					.map(nextValue -> convertToPredicate(nextValue)).toList()
					.toArray(new Predicate[0]);

			return addCase(patternMatcher.inCaseOfManyType(predicate, action,
					predicates));

		}
	}
	
	
	private <T,R> MatchingInstance<T,R> addCase(PatternMatcher o){
		return new MatchingInstance<>(this.withPatternMatcher(o));
	}


}