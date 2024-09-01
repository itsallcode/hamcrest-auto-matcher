/**
 * Hamcrest auto-matcher automatically creates Matchers with good error messages
 * in case of a test failure.
 */
module org.itsallcode.automatcher {
    exports org.itsallcode.matcher.auto;

    requires java.sql;
    requires java.logging;
    requires transitive org.hamcrest;
}
