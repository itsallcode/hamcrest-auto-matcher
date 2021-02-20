/**
 * Automatic hamcrest matcher for model classes
 * Copyright (C) 2017 Christoph Pirkl <christoph at users.sourceforge.net>
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
package com.github.hamstercommunity.matcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * This class allows reporting mismatches during comparison of two objects in a
 * matcher. It takes care of the overall matching state, i.e. when one attribute
 * does not match, the overall state is 'not matching'. Additionally it builds a
 * description of the mismatch.
 */
public class MismatchReporter {
	private final Description mismatchDescription;
	private boolean firstMismatch = true;
	private boolean matches = true;

	private MismatchReporter(final Description mismatchDescription) {
		this.mismatchDescription = mismatchDescription;
	}

	static MismatchReporter start(final Description mismatchDescription) {
		mismatchDescription.appendText("{");
		return new MismatchReporter(mismatchDescription);
	}

	boolean finishAndCheckMatching() {
		this.mismatchDescription.appendText("}");
		return this.matches;
	}

	/**
	 * Check if the actual object matches using the given {@link Matcher} and record
	 * a message if it does not match.
	 * 
	 * @param message the message to output in case of a failed match
	 * @param matcher the {@link Matcher} used for checking the actual value
	 * @param actual  the actual value that will be compared using the
	 *                {@link Matcher}.
	 * @param <T>     the type of the matcher.
	 * @return this {@link MismatchReporter} allowing a fluent programming style.
	 */
	public <T> MismatchReporter checkMismatch(final String message, final Matcher<T> matcher, final T actual) {
		if (!matcher.matches(actual)) {
			reportMismatch(message, matcher, actual);
			this.matches = false;
		}
		return this;
	}

	private void reportMismatch(final String name, final Matcher<?> matcher, final Object actual) {
		appendComma();
		this.mismatchDescription //
				.appendText(name) //
				.appendText(" ");
		matcher.describeMismatch(actual, this.mismatchDescription);
	}

	private void appendComma() {
		if (!this.firstMismatch) {
			this.mismatchDescription.appendText(", ");
		}
		this.firstMismatch = false;
	}
}
