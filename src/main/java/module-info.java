module hamcrest.auto.matcher {
	exports org.itsallcode.matcher;
	exports org.itsallcode.matcher.auto;
	exports org.itsallcode.matcher.config;

	requires static java.sql;
	requires java.logging;
	requires transitive org.hamcrest;
}
