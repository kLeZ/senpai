# Changelog

## [Unreleased]

## [1.2.1] - 2023-07-27
- Updated support for IntelliJ Platform 232.*
- Fixed CHANGELOG.md to add changes the previous release didn't cover because of errors in CI/CD
- Fixed the build of version 1.2.1 which returned a _simple_ `System.IO.IOException: No space left on device` while running in the GitHub Actions VM
- Added Free Disk Space job to build workflow to help with _No space left on device_

## [1.2.0] - 2023-07-26

### Updated
- Updated plugin template to v1.6.0

### Fixed
- Fixed "File not found" when path contains spaces (closes #senpai-8)

## [1.1.0] - 2023-02-14

### Added
- Added preliminary test useful to validate the testability of the code and to initialize the setup system.
- Added "now" parameter to template, already formatted with the right formatter to avoid poisoning the template with
  java code
- Added first version of the Thymeleaf template.
- Added extra lib to work with java.time (JEP 310) inside Thymeleaf.
- Added thymeleaf dependency and a draft template engine class to replace the custom html generator. This will improve
  readability and maintainability of the report template
  part of the plugin, and also it will be simpler to add new features to the generated page.

### Enabled
- Enabled JUnit 5.

### Fixed
- Fixed serialization/deserialization issue with filesComments and generalComments
- Fixed IndexOutOfBoundsException in brush property when filePath is empty
- Fixed bug with accessibility of the template from classpath. Wrong ClassLoader.

### Gradle
- Gradle 8.0 is not yet supported (don't know why, will investigate). Reverting back to 7.6.

### Moved
- Moved getBrush() as a public property of ReviewFileComment, it makes more sense and allows for more concise and
  idiomatic code.
- Moved sorting logic to the properties' getters. In this way we can rely on the fact that the collections are always
  sorted whenever we need to access them. This eases code
  both in the custom generator and in the Thymeleaf template, and it makes more sense to have an already sorted
  collection instead of sorting on the fly when needed.
- Moved isCommentWithoutContent() as a public property of ReviewComment, it makes more sense and allows for more concise
  and idiomatic code.

### Refactoring
- Refactoring strings and their manipulation.

### Small
- Small correction on default font not getting recovered with a generic family.

### Updated
- Updated Gradle to v8.0

## [1.0.0] - 2023-02-12

### Initial
- Initial commit

### Template
- Template cleanup

[Unreleased]: https://github.com/kLeZ/senpai/compare/v1.2.1...HEAD
[1.2.1]: https://github.com/kLeZ/senpai/compare/v1.2.0...v1.2.1
[1.2.0]: https://github.com/kLeZ/senpai/compare/v1.1.0...v1.2.0
[1.1.0]: https://github.com/kLeZ/senpai/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/kLeZ/senpai/commits/v1.0.0
