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
package com.github.hamstercommunity.matcher.map;

import java.util.Map;

import org.hamcrest.Factory;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

public class IsMapWithSize<K, V> extends FeatureMatcher<Map<? extends K, ? extends V>, Integer> {

	public IsMapWithSize(Matcher<? super Integer> sizeMatcher) {
		super(sizeMatcher, "a map with size", "map size");
	}

	@Override
	protected Integer featureValueOf(Map<? extends K, ? extends V> actual) {
		return actual.size();
	}

	/**
	 * Creates a matcher for {@link java.util.Map}s that matches when the
	 * <code>size()</code> method returns a value that satisfies the specified
	 * matcher.
	 * <p/>
	 * For example:
	 * 
	 * <pre>
	 * Map&lt;String, Integer&gt; map = new HashMap&lt;&gt;();
	 * map.put(&quot;key&quot;, 1);
	 * assertThat(map, isMapWithSize(equalTo(1)));
	 * </pre>
	 * 
	 * @param sizeMatcher
	 *            a matcher for the size of an examined {@link java.util.Map}
	 */
	@Factory
	public static <K, V> Matcher<Map<? extends K, ? extends V>> isMapWithSize(Matcher<? super Integer> sizeMatcher) {
		return new IsMapWithSize<>(sizeMatcher);
	}

	/**
	 * Creates a matcher for {@link java.util.Map}s that matches when the
	 * <code>size()</code> method returns a value equal to the specified
	 * <code>size</code>.
	 * <p/>
	 * For example:
	 * 
	 * <pre>
	 * Map&lt;String, Integer&gt; map = new HashMap&lt;&gt;();
	 * map.put(&quot;key&quot;, 1);
	 * assertThat(map, isMapWithSize(1));
	 * </pre>
	 * 
	 * @param size
	 *            the expected size of an examined {@link java.util.Map}
	 */
	@Factory
	public static <K, V> Matcher<Map<? extends K, ? extends V>> isMapWithSize(int size) {
		final Matcher<? super Integer> matcher = Matchers.equalTo(size);
		return IsMapWithSize.<K, V> isMapWithSize(matcher);
	}
}