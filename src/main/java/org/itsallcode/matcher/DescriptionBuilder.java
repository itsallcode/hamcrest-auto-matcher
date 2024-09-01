package org.itsallcode.matcher;

import org.hamcrest.*;

/**
 * This class builds a {@link Description} based on {@link Matcher} and a
 * description. It can be used for
 * {@link org.hamcrest.TypeSafeDiagnosingMatcher#describeTo(Description)}.
 */
public final class DescriptionBuilder {
    private final Description description;
    private boolean firstElement = true;

    private DescriptionBuilder(final Description description) {
        this.description = description;
    }

    static DescriptionBuilder start(final Description description) {
        description.appendText("{");
        return new DescriptionBuilder(description);
    }

    /**
     * Append a message and a matcher to the description.
     * 
     * @param message message to append
     * @param matcher matcher to append
     * @return {@code this} for method chaining
     */
    public DescriptionBuilder append(final String message, final SelfDescribing matcher) {
        appendComma();
        this.description.appendText(message) //
                .appendText("=") //
                .appendDescriptionOf(matcher);
        return this;
    }

    private void appendComma() {
        if (!this.firstElement) {
            this.description.appendText(", ");
        }
        this.firstElement = false;
    }

    void close() {
        this.description.appendText("}");
    }
}
