module org.itsallcode.automatcher {
	exports org.itsallcode.matcher.auto;

	requires java.sql;
	requires java.logging;
	requires transitive org.hamcrest;
}
