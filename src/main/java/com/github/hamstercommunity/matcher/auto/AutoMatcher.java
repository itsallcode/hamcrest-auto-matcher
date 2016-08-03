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
package com.github.hamstercommunity.matcher.auto;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.emptyIterable;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import com.github.hamstercommunity.matcher.config.ConfigurableMatcher;
import com.github.hamstercommunity.matcher.config.MatcherConfig;

/**
 * This class configures and creates a {@link ConfigurableMatcher} using
 * reflection.
 */
public class AutoMatcher {

	public static <T> Matcher<T> equalTo(T expected) {
		final MatcherConfig<T> config = new AutoConfigBuilder<>(expected).build();
		return new ConfigurableMatcher<>(config);
	}

	@SafeVarargs
	public static <T> Matcher<Iterable<? extends T>> contains(T... expected) {
		if (expected.length == 0) {
			return emptyIterable();
		}
		final List<Matcher<T>> itemMatchers = getMatchers(expected);
		@SuppressWarnings("unchecked")
		final Matcher<Iterable<? extends T>> listMatcher = Matchers.contains(itemMatchers.toArray(new Matcher[0]));
		return listMatcher;
	}

	@SafeVarargs
	public static <T> Matcher<Iterable<? extends T>> containsInAnyOrder(T... expected) {
		if (expected.length == 0) {
			return emptyIterable();
		}
		final List<Matcher<T>> itemMatchers = getMatchers(expected);
		@SuppressWarnings("unchecked")
		final Matcher<Iterable<? extends T>> listMatcher = Matchers
				.containsInAnyOrder(itemMatchers.toArray(new Matcher[0]));
		return listMatcher;
	}

	private static <T> List<Matcher<T>> getMatchers(T[] expected) {
		return Arrays.stream(expected) //
				.map(AutoMatcher::equalTo) //
				.collect(toList());
	}
}
