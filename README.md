# hamcrest-auto-matcher

[![Build Status](https://travis-ci.org/itsallcode/hamcrest-auto-matcher.svg?branch=master)](https://travis-ci.org/itsallcode/hamcrest-auto-matcher)
[![Download](https://api.bintray.com/packages/kaklakariada/maven/hamcrest-auto-matcher/images/download.svg)](https://bintray.com/kaklakariada/maven/hamcrest-auto-matcher/_latestVersion)

Automatic hamcrest matcher for model classes for Java 8

## Why use hamcrest-auto-matcher?

Writing a hamcrest matcher for your model classes by extending [`TypeSafeDiagnosingMatcher`](http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/TypeSafeDiagnosingMatcher.html) is a good idea, because it gives you a readable diff of actual and expected property values. But doing it by hand is tedious and hard to get right, especially for classes with many properties:
* It is easy to make mistakes in the [`matches()`](http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/TypeSafeDiagnosingMatcher.html#matches%28java.lang.Object%29) method.
* It requires lot's of boiler plate code.
* Good layout of actual and expected property values is hard to get right.
* Each property occurs in multiple places which violates the [DRY](https://en.wikipedia.org/wiki/Don't_repeat_yourself) principle.

## Requirements

Java 8

## How to use hamcrest-auto-matcher in your project

### Setup dependencies

#### Gradle
```groovy
repositories {
    jcenter()
}

dependencies {
    testCompile 'com.github.kaklakariada:hamcrest-auto-matcher:0.4.3'
}
```

#### Maven
```xml
<dependency>
    <groupId>com.github.kaklakariada</groupId>
    <artifactId>hamcrest-auto-matcher</artifactId>
    <version>0.4.3</version>
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
$ git clone https://github.com/itsallcode/hamcrest-auto-matcher.git
$ ./gradlew check
# Test report: build/reports/tests/index.html
```

### Using eclipse

Import into eclipse using [buildship](https://projects.eclipse.org/projects/tools.buildship).

### Generate license header for added files

```bash
$ ./gradlew licenseFormat
```

### Publish to jcenter

1. Create file `gradle.properties` in project directory with the following content and enter your bintray account:

    ```properties
    bintrayUser = <user>
    bintrayApiKey = <apiKey>
    ```

2. Increment version number in `build.gradle`, commit and push.
3. Run the following command:

    ```bash
    $ ./gradlew clean build check bintrayUpload -i
    ```

4. Create a new [release](https://github.com/itsallcode/hamcrest-auto-matcher/releases) on GitHub.
5. Sign in at https://bintray.com/ and publish the uploaded artifacts.
