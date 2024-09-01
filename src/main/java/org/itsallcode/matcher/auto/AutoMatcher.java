package org.itsallcode.matcher.auto;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.emptyIterable;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.itsallcode.matcher.config.ConfigurableMatcher;

/**
 * This class configures and creates a {@link ConfigurableMatcher} using
 * reflection.
 */
public final class AutoMatcher {

	private AutoMatcher() {
		// not instantiable
	}

	/**
	 * Creates a matcher that matches the expected object.
	 * 
	 * @param <T>      type of the expected object
	 * @param expected expected object
	 * @return a matcher that matches the expected object
	 */
	public static <T> Matcher<T> equalTo(final T expected) {
		return AutoConfigBuilder.createEqualToMatcher(expected);
	}

	/**
	 * Creates a matcher that matches an iterable containing the expected elements
	 * in the same order.
	 * 
	 * @param <T>      type of the elements in the iterable
	 * @param expected expected elements
	 * @return a matcher that matches an iterable containing the expected elements
	 *         in the same order
	 */
	@SafeVarargs
	@SuppressWarnings({ "java:S1452", "java:S923", "varargs" }) // Wildcard type required here
	public static <T> Matcher<Iterable<? extends T>> contains(final T... expected) {
		if (expected.length == 0) {
			return emptyIterable();
		}
		return Matchers.contains(getMatchers(expected));
	}

	/**
	 * Creates a matcher that matches an iterable containing the expected elements
	 * in any order.
	 * 
	 * @param <T>      type of the elements in the iterable
	 * @param expected expected elements
	 * @return a matcher that matches an iterable containing the expected elements
	 *         in any order
	 */
	@SafeVarargs
	@SuppressWarnings({ "java:S1452", "java:S923", "varargs" }) // Wildcard type required here
	public static <T> Matcher<Iterable<? extends T>> containsInAnyOrder(final T... expected) {
		if (expected.length == 0) {
			return emptyIterable();
		}
		return Matchers.containsInAnyOrder(getMatchers(expected));
	}

	private static <T> List<Matcher<? super T>> getMatchers(final T[] expected) {
		return Arrays.stream(expected) //
				.map(AutoMatcher::equalTo) //
				.collect(toList());
	}
}
