package org.itsallcode.matcher.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.function.Function;

import org.hamcrest.*;
import org.j8unit.J8UnitTest;
import org.junit.Test;

/**
 * This is the base class for all tests of {@link TypeSafeDiagnosingMatcher}.
 *
 * @param <T> the type compared by the {@link TypeSafeDiagnosingMatcher} under
 *            test.
 */
public interface MatcherTestBase<T> extends J8UnitTest<Function<T, Matcher<T>>> {

	@Test(expected = NullPointerException.class)
	public default void testNullObject() {
		createMatcher(null);
	}

	default void assertMatch(final T object) {
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
	default void assertFailureDescription(final String expectedDescription, final String actualDescription,
			final T expected,
			final T actual) {

		assertNoMatch(expected, actual);

		final String expectedExceptionMessage = "\nExpected: " + expectedDescription + "\n" //
				+ "     but: " + actualDescription;
		try {
			assertThat(actual, createMatcher(expected));
			fail("Expected AssertionError");
		} catch (final AssertionError e) {
			assertThat(e, instanceOf(AssertionError.class));
			assertEquals(expectedExceptionMessage, e.getMessage());
			assertThat(e.getMessage(), equalTo(expectedExceptionMessage));
		}
	}

	/**
	 * Create a matcher for the given expected object.
	 * 
	 * @param expected the expected object
	 * @return a matcher for the given expected object
	 */
	default Matcher<? super T> createMatcher(final T expected) {
		return createNewSUT().apply(expected);
	}

	default void assertNoMatch(final T objectA, final T objectB) {
		assertMatch(objectA);
		assertMatch(objectB);
		assertThat(objectA, not(createMatcher(objectB)));
		assertThat(objectB, not(createMatcher(objectA)));

		assertThat(getDescription(objectA), not(equalTo(getDescription(objectB))));
		assertThat(getDescription(objectA), equalTo(getDescription(objectA)));
		assertThat(getDescription(objectB), equalTo(getDescription(objectB)));
	}

	default String getDescription(final T object) {
		final StringDescription description = new StringDescription();
		createMatcher(object).describeTo(description);
		return description.toString();
	}
}
