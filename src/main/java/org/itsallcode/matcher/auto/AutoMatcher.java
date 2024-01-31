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
public class AutoMatcher {

	private AutoMatcher() {
		// not instantiable
	}

	public static <T> Matcher<T> equalTo(final T expected) {
		return AutoConfigBuilder.createEqualToMatcher(expected);
	}

	@SafeVarargs
	@SuppressWarnings({ "java:S1452", "varargs" }) // Wildcard type required here
	public static <T> Matcher<Iterable<? extends T>> contains(final T... expected) {
		if (expected.length == 0) {
			return emptyIterable();
		}
		return Matchers.contains(getMatchers(expected));
	}

	@SafeVarargs
	@SuppressWarnings({ "java:S1452", "varargs" }) // Wildcard type required here
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
