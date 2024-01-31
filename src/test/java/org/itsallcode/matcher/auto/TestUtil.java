package org.itsallcode.matcher.auto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

import org.itsallcode.matcher.auto.AutoMatcher;

public class TestUtil {
	private TestUtil() {
		// not instantiable
	}

	public static <T> void assertValuesMatch(T value1, T value2) {
		assertThat(value1, AutoMatcher.equalTo(value1));
		assertThat(value2, AutoMatcher.equalTo(value2));
		assertThat(value2, AutoMatcher.equalTo(value1));
		assertThat(value1, AutoMatcher.equalTo(value2));
	}

	public static <T> void assertValuesDoNotMatch(T value1, T value2) {
		assertThat("expect not match", value1, not(AutoMatcher.equalTo(value2)));
		assertThat("expect match", value1, AutoMatcher.equalTo(value1));
		assertThat("expect match", value2, AutoMatcher.equalTo(value2));
	}
}