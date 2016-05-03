package com.github.hamstercommunity.matcher.auto;

import org.hamcrest.Matcher;

import com.github.hamstercommunity.matcher.config.ConfigurableMatcher;
import com.github.hamstercommunity.matcher.config.MatcherConfig;

/**
 * This class configures and creates a {@link ConfigurableMatcher} using
 * reflection.
 */
public class AutoMatcher {

	public static <T> Matcher<T> equalTo(T expected) {
		final MatcherConfig<T> config = new AutoConfigBuilder<T>(expected).build();
		return new ConfigurableMatcher<>(config);
	}
}
