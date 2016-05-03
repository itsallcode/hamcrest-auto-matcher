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
package com.github.hamstercommunity.matcher.config;

import java.util.function.Function;

import org.hamcrest.Matcher;

/**
 * @param <T>
 *            base type of the {@link Matcher}
 * @param <P>
 *            type of the property
 */
class PropertyConfig<T, P> {
	private final String propertyName;
	private final Matcher<P> matcher;
	private final Function<T, P> propertyAccessor;

	PropertyConfig(final String propertyName, final Matcher<P> matcher, final Function<T, P> propertyAccessor) {
		this.propertyName = propertyName;
		this.matcher = matcher;
		this.propertyAccessor = propertyAccessor;
	}

	public String getPropertyName() {
		return this.propertyName;
	}

	public Matcher<P> getMatcher() {
		return this.matcher;
	}

	public P getPropertyValue(final T object) {
		return this.propertyAccessor.apply(object);
	}
}