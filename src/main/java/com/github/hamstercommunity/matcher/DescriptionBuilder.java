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
package com.github.hamstercommunity.matcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;

/**
 * This class builds a {@link Description} based on {@link Matcher} and a
 * description. It can be used for
 * {@link org.hamcrest.TypeSafeDiagnosingMatcher.TypeSafeDiagnosingMatcher#describeTo(Description)}
 * .
 */
public class DescriptionBuilder {
	private final Description description;
	private boolean firstElement = true;

	private DescriptionBuilder(final Description description) {
		this.description = description;
	}

	static DescriptionBuilder start(final Description description) {
		description.appendText("{");
		return new DescriptionBuilder(description);
	}

	public DescriptionBuilder append(final String message, final SelfDescribing matcher) {
		if (!this.firstElement) {
			this.description.appendText(", ");
		}
		this.description.appendText(message).appendText("=").appendDescriptionOf(matcher);
		this.firstElement = false;
		return this;
	}

	public void close() {
		this.description.appendText("}");
	}
}
