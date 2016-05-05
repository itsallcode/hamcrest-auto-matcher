# hamcrest-auto-matcher

[![Build Status](https://travis-ci.org/hamstercommunity/hamcrest-auto-matcher.svg?branch=master)](https://travis-ci.org/hamstercommunity/hamcrest-auto-matcher)
[![Download](https://api.bintray.com/packages/kaklakariada/maven/hamcrest-auto-matcher/images/download.svg)](https://bintray.com/kaklakariada/maven/hamcrest-auto-matcher/_latestVersion)

Automatic hamcrest matcher for model classes

## Usage

### Setup dependencies

* Add jcenter maven repository:
```groovy
repositories {
    jcenter()
}
```
* Add dependency
```groovy
dependencies {
    testCompile 'com.github.kaklakariada:hamcrest-auto-matcher:0.2.0'
}
```
```xml
<dependency>
	<groupId>com.github.kaklakariada</groupId>
	<artifactId>hamcrest-auto-matcher</artifactId>
	<version>0.2.0</version>
	<scope>test</scope>
</dependency>
```

### Using `ConfigurableMatcher`
Create a matcher for your model class by extending [`ConfigurableMatcher`](src/main/java/com/github/hamstercommunity/matcher/ConfigurableMatcher.java), see [`DemoModelMatcher`](src/test/java/com/github/hamstercommunity/matcher/model/DemoModelMatcher.java) as an example.

This allows you to specify properties and custom property matchers.

### Using `AutoMatcher` 
Use [`AutoMatcher.equalTo()`](src/main/java/com/github/hamstercommunity/matcher/auto/AutoMatcher.java) to create a matcher for your expected model instance. This will use reflection to determine the expected values based on getter methods.

## Development

### Using eclipse

Import into eclipse using [buildship](https://projects.eclipse.org/projects/tools.buildship).

### Generate license header for added files:

```bash
./gradlew licenseFormatMain licenseFormatTest
```

### Publish to jcenter

* Create file `gradle.properties` in project directory with the following content and enter your bintray account:
```
bintrayUser = <user>
bintrayApiKey = <apiKey>
```
* Increment version number in `build.gradle`
* Run the following command:
```bash
./gradlew clean check bintrayUpload -i
```