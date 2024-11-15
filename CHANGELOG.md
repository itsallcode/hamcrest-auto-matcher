# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.9.0] - unreleased

## [0.8.2] - 2024-11-13

* [#18](https://github.com/itsallcode/hamcrest-auto-matcher/issues/18): Ignore order for Sets

## [0.8.1] - 2024-09-12

* [#17](https://github.com/itsallcode/hamcrest-auto-matcher/pull/17): Allow using `null` as expected value

## [0.8.0] - 2024-09-01

### Changes
* [#12](https://github.com/itsallcode/hamcrest-auto-matcher/issues/12): Add support for matching Java 17 records
  * `AutoConfigBuilder` now verifies, that the class has at least one property to avoid succeeding assertions that should fail
  * Upgrade from `org.hamcrest:hamcrest:2.2` to `org.hamcrest:hamcrest:3.0`
* [#14](https://github.com/itsallcode/hamcrest-auto-matcher/pull/14): Fix build with Java 21
* [#15](https://github.com/itsallcode/hamcrest-auto-matcher/pull/15): Format sources
* [#16](https://github.com/itsallcode/hamcrest-auto-matcher/pull/16): Automate release process

## [0.7.0] - 2024-04-21

### Breaking Changes

* [#8](https://github.com/itsallcode/hamcrest-auto-matcher/issues/8) Added `module-info.java` with module name `org.itsallcode.automatcher`
* [#10](https://github.com/itsallcode/hamcrest-auto-matcher/pull/10) Upgrade from `org.hamcrest:hamcrest-all:1.3` to `org.hamcrest:hamcrest:2.2`

## [0.6.0] - 2024-01-31

### Breaking Changes

* [#6](https://github.com/itsallcode/hamcrest-auto-matcher/pull/6):  Rename packages to `org.itsallcode`
* [#5](https://github.com/itsallcode/hamcrest-auto-matcher/issues/5): Add support for `Optional` and other types
