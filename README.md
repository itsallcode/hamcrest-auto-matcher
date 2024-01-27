# hamcrest-auto-matcher

Automatic hamcrest matcher for model classes for Java 11

[![Build](https://github.com/itsallcode/hamcrest-auto-matcher/actions/workflows/build.yml/badge.svg)](https://github.com/itsallcode/hamcrest-auto-matcher/actions/workflows/build.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=org.itsallcode%3Ahamcrest-auto-matcher&metric=alert_status)](https://sonarcloud.io/dashboard?id=org.itsallcode%3Ahamcrest-auto-matcher)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=org.itsallcode%3Ahamcrest-auto-matcher&metric=coverage)](https://sonarcloud.io/dashboard?id=org.itsallcode%3Ahamcrest-auto-matcher)
[![Maven Central](https://img.shields.io/maven-central/v/org.itsallcode/hamcrest-auto-matcher)](https://search.maven.org/artifact/org.itsallcode/hamcrest-auto-matcher)

## Why use hamcrest-auto-matcher?

Writing a hamcrest matcher for your model classes by extending [`TypeSafeDiagnosingMatcher`](http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/TypeSafeDiagnosingMatcher.html) is a good idea, because it gives you a readable diff of actual and expected property values. But doing it by hand is tedious and hard to get right, especially for classes with many properties:
* It is easy to make mistakes in the [`matches()`](http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/TypeSafeDiagnosingMatcher.html#matches%28java.lang.Object%29) method.
* It requires lot's of boiler plate code.
* Good layout of actual and expected property values is hard to get right.
* Each property occurs in multiple places which violates the [DRY](https://en.wikipedia.org/wiki/Don't_repeat_yourself) principle.

## Requirements

Java 11

## How to use hamcrest-auto-matcher in your project

### Setup dependencies

#### Gradle
```groovy
repositories {
    mavenCentral()
}

dependencies {
    testCompile 'org.itsallcode:hamcrest-auto-matcher:0.5.0'
}
```

#### Maven
```xml
<dependency>
    <groupId>org.itsallcode</groupId>
    <artifactId>hamcrest-auto-matcher</artifactId>
    <version>0.5.0</version>
    <scope>test</scope>
</dependency>
```

### Using [`AutoMatcher`](src/main/java/com/github/hamstercommunity/matcher/auto/AutoMatcher.java)

Assume you have two model classes [`DemoModel`](src/test/java/com/github/hamstercommunity/matcher/model/DemoModel.java) and [`DemoAttribute`](src/test/java/com/github/hamstercommunity/matcher/model/DemoAttribute.java):

```java
public class DemoModel {
    private final int id;
    private final String name;
    private final DemoAttribute attr;
    private final List<DemoModel> children;
    private final String[] stringArray;
    private final Long longVal;

    public DemoModel(int id, String name, Long longVal, DemoAttribute attr, String[] stringArray,
            List<DemoModel> children) {
        this.id = id;
        this.name = name;
        this.longVal = longVal;
        this.attr = attr;
        this.stringArray = stringArray;
        this.children = children;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DemoAttribute getAttr() {
        return attr;
    }

    public List<DemoModel> getChildren() {
        return children;
    }

    public String[] getStringArray() {
        return stringArray;
    }

    public Long getLongVal() {
        return longVal;
    }

    @Override
    public String toString() {
        return "DemoModel [id=" + id + ", name=" + name + ", attr=" + attr + ", children=" + children + ", stringArray="
                + Arrays.toString(stringArray) + ", longVal=" + longVal + "]";
    }
}

public class DemoAttribute {
    private final String value;

    public DemoAttribute(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "DemoAttribute [value=" + value + "]";
    }
}
```

Use [`AutoMatcher.equalTo()`](src/main/java/com/github/hamstercommunity/matcher/auto/AutoMatcher.java) to create a matcher for your expected model instance. This will use reflection to determine expected property values based on getter methods:

```java
DemoModel expected = ...;
DemoModel actual = ...;
assertThat(actual, AutoMatcher.equalTo(expected));
```

Example mismatch report:
```
Expected: {id=<4711>, longVal=null, name="name1", attr=null, stringArray=null, children=null}
     but: {longVal was <42L>}
```

### Using [`ConfigurableMatcher`](src/main/java/com/github/hamstercommunity/matcher/config/ConfigurableMatcher.java)
If `AutoMatcher` does not work for your model classes, you can still use [`ConfigurableMatcher`](src/main/java/com/github/hamstercommunity/matcher/config/ConfigurableMatcher.java) and [`MatcherConfig`](src/main/java/com/github/hamstercommunity/matcher/config/MatcherConfig.java) which allows you to specify properties and custom matchers explicitly but is much easier to use than `TypeSafeDiagnosingMatcher`.

```java
public class DemoModelMatcher {
    public static Matcher<DemoModel> equalTo(DemoModel expected) {
        final MatcherConfig<DemoModel> config = MatcherConfig.builder(expected)
                .addEqualsProperty("id", DemoModel::getId)
                .addEqualsProperty("longVal", DemoModel::getLongVal)
                .addEqualsProperty("name", DemoModel::getName)
                .addProperty("attr", DemoModel::getAttr, DemoAttributeMatcher::equalTo)
                .addEqualsProperty("stringArray", DemoModel::getStringArray)
                .addIterableProperty("children", DemoModel::getChildren, DemoModelMatcher::equalTo)
                .build();
        return new ConfigurableMatcher<>(config);
    }
}
```
Also see [`DemoModelMatcher`](src/test/java/com/github/hamstercommunity/matcher/model/DemoModelMatcher.java) as an example.

## Development

```bash
git clone https://github.com/itsallcode/hamcrest-auto-matcher.git
cd hamcrest-auto-matcher
./gradlew check
# Test report: build/reports/tests/index.html
```

### Using eclipse

Import into eclipse using [buildship](https://projects.eclipse.org/projects/tools.buildship).

### Run sonar analysis

```bash
./gradlew clean sonar --info -Dsonar.token=[token]
```

### Check for dependency updates

```bash
./gradlew dependencyUpdates
```


### Test Coverage

To calculate and view test coverage:

```sh
./gradlew check jacocoTestReport
open build/reports/jacoco/test/html/index.html
```

### Publish to Maven Central

1. Add the following to your `~/.gradle/gradle.properties`:

    ```properties
    ossrhUsername=<your maven central username>
    ossrhPassword=<your maven central passwort>

    signing.keyId=<gpg key id (last 8 chars)>
    signing.password=<gpg key password>
    signing.secretKeyRingFile=<path to secret keyring file>
    ```

2. Increment version number in `build.gradle` and `README.md`, commit and push.
3. Optional: run the following command to do a dry-run:

    ```sh
    ./gradlew clean check build publishToSonatype closeSonatypeStagingRepository --info
    ```

4. Run the following command to publish to Maven Central:

    ```sh
    ./gradlew clean check build publishToSonatype closeAndReleaseSonatypeStagingRepository --info
    ```

5. Create a new [release](https://github.com/itsallcode/hamcrest-auto-matcher/releases) on GitHub.
6. After some time the release will be available at [Maven Central](https://repo1.maven.org/maven2/org/itsallcode/hamcrest-auto-matcher/).
