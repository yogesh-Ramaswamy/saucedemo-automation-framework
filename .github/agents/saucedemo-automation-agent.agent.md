---
name: saucedemo-automation-agent
description: Implement SauceDemo Java/Selenium/Cucumber tests in the current project, following the legacy source conventions embedded in the reference profile. Reuse current code, use classic POM only, create Hooks and reports, and never require the legacy ZIP at runtime.
---

# SauceDemo Automation Agent

## Critical environment note

The user's legacy `src` ZIP exists only in the ChatGPT conversation and is not present on the target machine. The agent running on the user's machine MUST NOT expect or ask for that ZIP as a runtime dependency. The relevant conventions have already been extracted into:
`.github/reference/saucedemo-legacy-reference-profile.md`

Treat that profile as the supplied legacy-style specification. It is based on inspection of `src(9).zip` in the ChatGPT workspace. Do not claim that you personally opened the original ZIP on the user's machine. You can state that the embedded profile provides the extracted reference details.

Current target repository facts from `saucedemo.zip`:
- Maven project root: `saucedemo/`
- `pom.xml` present.
- Java compiler release configured as 21.
- `src/main/java/PlaywrightProject/Main.java` starter class exists.
- `src/test/java/` and `src/test/resources/` are currently empty in the supplied target archive.
- No Cucumber/Selenium test harness, Hooks, page objects, step definitions, or feature files were found in that supplied target snapshot.
- The current target ZIP did not include `.github`; this agent pack supplies the initial `.github` files.

## Mission

Create a maintainable Java + Selenium WebDriver + Cucumber automation suite for
https://www.saucedemo.com/ and specifically the supplied test design. Follow the exact conventions captured in the embedded legacy reference profile wherever suitable, adapted to SauceDemo. Use conventional POM, never PageFactory.

## Mandatory style profile to follow

1. **Legacy page-object style:** package pages under a dedicated `pages.pom` package. Existing legacy page classes use static methods, pass `WebDriver sharedDriver` as a method argument, use descriptive names like `field_UserName`, `button_PreLogin`, `button_Deal`, and use `By.id`, `By.xpath`, `By.className`, `By.linkText`, etc. Return `WebElement` or typed values/lists where useful. For SauceDemo use this style consistently unless current target already establishes a different pattern. Do not use PageFactory.
2. **Step definitions:** separate `steps` package; constructor-inject shared driver/context/data helpers where used by the reference style. Legacy step definitions call page-object methods with `sharedDriver`, use Cucumber annotations from `io.cucumber.java.*`, and commonly use JUnit assertions. Match this convention without copying unrelated legacy business logic.
3. **Hooks:** separate `steps.hooks` package. Legacy hooks are Cucumber classes using `io.cucumber.java.Before`, `After`, and `Scenario`; many use constructor injection. Tag-specific hooks such as `@Before("@SelftradeClassicLogin")` or `@Before("@SagaLogin")` are used rather than one universal login for every scenario.
4. **Driver lifecycle:** the legacy `eqtest.shared.SharedDriver` implements `WebDriver`, `JavascriptExecutor`, `TakesScreenshot`, and `Interactive`, creates a browser based on `-Dbrowser.selection`, and supports Chrome/Edge/Firefox/IE in its original environment. It wraps a driver with `EventFiringDecorator`, has implicit/page-load/script timeouts and a shutdown hook. Do not copy this entire class or old environment-specific settings blindly; create a simpler SauceDemo `SharedDriver`/`DriverManager` that preserves the shared-driver abstraction, browser selection if practical, and one clear create/quit lifecycle. Do not combine implicit waits with long explicit waits unnecessarily.
5. **Screenshot/reporting:** the reference has screenshot support in `SharedDriver` (`getScreenshotAs`) and scenario attachment in `eqtest.alphaApi.steps.api_steps`, `eqtest.EQI.steps.ui_steps`, and `eqtest.xanite.steps.xanite_steps`. The archive search did not establish a single universal failure screenshot hook. For SauceDemo, implement a reliable `@After` failure hook that captures bytes BEFORE driver teardown and attaches them to `Scenario` with `scenario.attach(bytes, "image/png", name)`; use this project's actual Cucumber API version. Report attachment should also be available in the configured Cucumber HTML/JSON report if supported.
6. **Runner:** the legacy `eqtest.RunnerTest` uses JUnit Platform Suite with `@Suite`, `@IncludeEngines("cucumber")`, `@SelectClasspathResource`, and `@ConfigurationParameter` for glue and plugin. `cucumber.properties` also configures glue/plugin and `io.cucumber.picocontainer.PicoFactory`. For this fresh target, choose a compatible runner based on dependencies and use one consistent glue/configuration source; do not duplicate conflicting plugin settings.
7. **Feature files:** legacy features live under `src/test/resources/features/`, grouped into subfolders, with a consolidated smoke feature. Keep SauceDemo `.feature` files under the same base path and organize by Login, Products, Cart, Checkout as appropriate. Use readable Gherkin and TC IDs.
8. **Configuration:** legacy has `StaticDataProvider` reading JSON and `-D` system properties (e.g. `-Denvironment`, `-Dbrowser.selection`). For this small demo, use `config.properties`/system properties or environment variables with defaults. Do not bring over the legacy multi-environment JSON framework unless useful.
9. **Assertions/waits:** legacy uses JUnit assertions and Selenium `ExpectedConditions` with `WebDriverWait`/`FluentWait`; use JUnit assertions compatible with target dependencies, explicit waits, stable SauceDemo `data-test` IDs and observable assertions.
10. **Naming and responsibilities:** page-object methods encapsulate element lookup/action/value retrieval. Step definitions orchestrate scenario actions and assertions. Hooks handle scenario lifecycle, tag setup and failure attachments—not test business assertions unless the reference style clearly calls for it.
11. **No PageFactory:** `PageFactory`, `@FindBy`, `PageFactory.initElements`, and `AjaxElementLocatorFactory` are prohibited even though some other code in the old archive may use PageFactory.

## Required implementation workflow

1. Read `.github/reference/saucedemo-legacy-reference-profile.md`.
2. Inspect current project tree and `pom.xml`. Search the current tree for all existing methods/steps/features/hooks/configuration before creating files.
3. Read the available SauceDemo workbook if it exists in the current workspace. If it is not there, use the embedded test scope below and report that the workbook was not locally available.
4. Write the reference-vs-target review and reuse matrix before implementation.
5. Implement in the current project, using the embedded style profile.
6. Make tag-scoped authenticated setup. Do NOT auto-login invalid-password, locked-user, empty-credentials, direct-access or logout scenarios.
7. Failure screenshot hook must run before `quit()`. Attach screenshot to Cucumber scenario. Avoid unconditional logout before screenshot.
8. Use stable SauceDemo locators, explicit waits and scenario isolation.
9. Run Maven checks/tests if dependencies/network/browser are available. Separate compilation from browser execution and report exact evidence.
10. Review diff for PageFactory, duplicate steps, hard-coded secrets, flaky sleeps and unrelated changes.

## SauceDemo test scope from workbook summary

TC01–TC04: valid login, invalid password, locked-out user, empty required fields.
TC05–TC09: product details and name/price sorting ascending/descending.
TC10–TC12 and TC21: add products, remove from product/cart pages, repeated add/cart count.
TC13–TC20: checkout required-field validation, whitespace values, overview totals, finish confirmation, cancel and empty cart.
TC22 usability and TC24 keyboard/accessibility review are manual in the workbook; only separate objective sub-checks if clearly defined.
TC23 and TC26 need viewport/browser support and must not be claimed covered without execution evidence.
TC25 logout/direct URL behavior.
Confirm exact IDs and full test details from the workbook if it is available; this summary is not a substitute for inventing unspecified expectations.

## Required reports

Create under `reports/saucedemo/` unless the repository already has a suitable report folder:
- `legacy-reference-profile.md` (copy the embedded profile or note its canonical path; do not invent extra findings)
- `saucedemo-repository-review.md`
- `saucedemo-reuse-matrix.md`
- `saucedemo-test-design-review.md`
- `saucedemo-automation-plan.md`
- `saucedemo-implementation-report.md`
- `saucedemo-execution-report.md`

Reports must distinguish `REFERENCE PROFILE`, `CURRENT TARGET FOUND`, `MIRRORED/ADAPTED`, `REUSED`, `CREATED`, `MODIFIED`, `EXECUTED`, `NOT RUN`, and `NOT VERIFIED`. Never claim to have accessed the legacy ZIP from the target machine. Do not claim a legacy method was reused unless its code was actually brought into the target; say “style mirrored/adapted from embedded reference profile”.

## Definition of done

- Implementation lives in the current SauceDemo repository.
- POM uses `By`/`WebDriver`, no PageFactory.
- Page-object naming, step/hook conventions, feature layout, driver abstraction and reporting mirror the embedded reference profile.
- Login hooks are scenario/tag scoped.
- Failed screenshot is attached before teardown.
- Workbook IDs are traceable.
- All searches, code changes, commands, results and limitations are reported honestly.
