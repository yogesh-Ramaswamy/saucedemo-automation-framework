# saucedemo-automation-framework
AI-assisted SauceDemo test automation framework using Java, Selenium WebDriver, Cucumber BDD and Maven, with automated test reports.
# SauceDemo Automation Agent Pack v4

This version embeds the legacy `src` conventions inside `.github/reference/saucedemo-legacy-reference-profile.md`.

**The old `src.zip` does not need to be copied to your machine.** Its relevant structure, POM style, step-definition style, hooks, driver, screenshot attachment, runner and config patterns have been extracted and written into the profile.

## Install
1. Extract this pack.
2. Merge `.github/agents`, `.github/instructions`, `.github/prompts`, and `.github/reference` into the current `saucedemo` repository.
3. Keep your existing unrelated `.github` files.
4. Make sure `Sauce_Demo_Test_Suite.xlsx` is available to your coding agent if you want it to read every cell/sheet; otherwise the embedded scope summary will be used and the report will state the workbook was not available.
5. Open the current project and run `.github/prompts/automate-saucedemo.prompt.md`.

## Embedded legacy conventions
- `pages.pom` package organization
- static page methods accepting `WebDriver sharedDriver`
- descriptive element method names such as `field_UserName`, `button_PreLogin`
- separate Cucumber steps and `steps.hooks` packages
- tag-scoped `@Before` setup and `@After` behavior
- shared-driver abstraction and browser system property pattern
- `Scenario.attach` screenshot evidence and Cucumber JUnit Platform runner/configuration
- JUnit assertions and Selenium explicit waits

## Adaptation constraints
- Classic POM only; no PageFactory.
- Do not copy legacy app-specific methods or environment-specific browser configuration.
- Failed screenshots must be captured and attached before browser teardown.
- Do not automatically log in negative login/logout tests.
- Report facts, changes and execution honestly.

## Implemented framework

The project now contains a Java 21, Selenium WebDriver, Cucumber, PicoContainer,
and JUnit Platform test framework under `src/test/`.

- Page objects: `src/test/java/saucedemo/pages/pom/`
- Step definitions: `src/test/java/saucedemo/steps/`
- Hooks: `src/test/java/saucedemo/steps/hooks/`
- Features: `src/test/resources/features/`
- Configuration: `src/test/resources/config.properties`
- Runner: `src/test/java/saucedemo/RunCucumberTest.java`
- Review and execution reports: `reports/saucedemo/`

Set `JAVA_HOME` to a Java 21 JDK before running Maven. Configuration values can
be overridden with `-D` properties.

```powershell
$env:JAVA_HOME = 'C:\Program Files\Java\jdk21'
mvn test -Dbrowser.headless=true
mvn test '-Dcucumber.filter.tags=not @TC16 and not @TC20' -Dbrowser.headless=true
mvn test '-Dcucumber.filter.tags=@TC26' -Dbrowser.selection=firefox -Dbrowser.headless=true
```

Generated Cucumber HTML and JSON reports are written to
`target/cucumber-reports/`. TC16 and TC20 intentionally assert workbook
assumptions that the current application does not satisfy; see
`reports/saucedemo/saucedemo-execution-report.md`.
