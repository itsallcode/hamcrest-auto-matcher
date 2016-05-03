/**
 * Automatic hamcrest matcher for model classes
 * Copyright (C) 2016 Christoph Pirkl <christoph at users.sourceforge.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.hamstercommunity.matcher.test;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.Test;

/**
 * This is the base class for all tests of {@link TypeSafeDiagnosingMatcher}.
 *
 * @param <T>
 *            the type compared by the {@link TypeSafeDiagnosingMatcher} under
 *            test.
 */
public abstract class MatcherTestBase<T> {

	@Test(expected = NullPointerException.class)
	public void testNullObject() {
		createMatcher(null);
	}

	protected void assertMatch(final T object) {
		assertThat(object, createMatcher(object));
		assertThat(getDescription(object), equalTo(getDescription(object)));
	}

	/**
	 * Verify that matching expected and actual fails with the correct error
	 * message.
	 * 
	 * @param expectedDescription
	 *            the description of the expected object
	 * @param actualDescription
	 *            the description of the actual object
	 * @param expected
	 *            the expected object being compared
	 * @param actual
	 *            the actual object being compared
	 */
	protected void assertFailureDescription(String expectedDescription, String actualDescription, final T expected,
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
	 * @param expected
	 *            the expected object
	 * @return a matcher for the given expected object
	 */
	protected abstract Matcher<? super T> createMatcher(final T expected);

	private void assertNoMatch(final T objectA, final T objectB) {
		assertMatch(objectA);
		assertMatch(objectB);
		assertThat(objectA, not(createMatcher(objectB)));
		assertThat(objectB, not(createMatcher(objectA)));

		assertThat(getDescription(objectA), not(equalTo(getDescription(objectB))));
		assertThat(getDescription(objectA), equalTo(getDescription(objectA)));
		assertThat(getDescription(objectB), equalTo(getDescription(objectB)));
	}

	private String getDescription(final T object) {
		final StringDescription description = new StringDescription();
		createMatcher(object).describeTo(description);
		return description.toString();
	}
}
