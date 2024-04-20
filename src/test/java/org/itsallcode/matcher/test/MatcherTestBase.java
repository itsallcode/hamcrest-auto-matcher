package org.itsallcode.matcher.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.function.Function;

import org.hamcrest.*;
import org.junit.jupiter.api.Test;

/**
 * This is the base class for all tests of {@link TypeSafeDiagnosingMatcher}.
 *
 * @param <T> the type compared by the {@link TypeSafeDiagnosingMatcher} under
 *            test.
 */
public abstract class MatcherTestBase<T> {

	protected abstract Function<T, Matcher<T>> createNewSUT();

	@Test
	void testNullObject() {
		assertThrows(NullPointerException.class, () -> createMatcher(null));
	}

	protected void assertMatch(final T object) {
		assertThat(object, createMatcher(object));
		assertThat(getDescription(object), equalTo(getDescription(object)));
	}

	/**
	 * Verify that matching expected and actual fails with the correct error
	 * message.
	 * 
	 * @param expectedDescription the description of the expected object
	 * @param actualDescription   the description of the actual object
	 * @param expected            the expected object being compared
	 * @param actual              the actual object being compared
	 */
	protected void assertFailureDescription(final String expectedDescription, final String actualDescription,
			final T expected,
			final T actual) {

		assertNoMatch(expected, actual);

		final String expectedExceptionMessage = "\nExpected: " + expectedDescription + "\n" //
				+ "     but: " + actualDescription;
		final Matcher<? super T> matcher = createMatcher(expected);
		final AssertionError error = assertThrows(AssertionError.class, () -> assertThat(actual, matcher));
		assertThat(error, instanceOf(AssertionError.class));
		assertEquals(expectedExceptionMessage, error.getMessage());
		assertThat(error.getMessage(), equalTo(expectedExceptionMessage));
	}

	/**
	 * Create a matcher for the given expected object.
	 * 
	 * @param expected the expected object
	 * @return a matcher for the given expected object
	 */
	protected Matcher<? super T> createMatcher(final T expected) {
		return createNewSUT().apply(expected);
	}

	protected void assertNoMatch(final T objectA, final T objectB) {
		assertMatch(objectA);
		assertMatch(objectB);
		assertThat(objectA, not(createMatcher(objectB)));
		assertThat(objectB, not(createMatcher(objectA)));

		assertThat(getDescription(objectA), not(equalTo(getDescription(objectB))));
		assertThat(getDescription(objectA), equalTo(getDescription(objectA)));
		assertThat(getDescription(objectB), equalTo(getDescription(objectB)));
	}

	protected String getDescription(final T object) {
		final StringDescription description = new StringDescription();
		createMatcher(object).describeTo(description);
		return description.toString();
	}
}
