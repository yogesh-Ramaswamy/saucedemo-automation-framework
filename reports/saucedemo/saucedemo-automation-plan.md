# SauceDemo Automation Plan

**Status:** `IMPLEMENTED` and validated  
**Review date:** 2026-10-29

## Implemented outcome

A Java 21 Selenium WebDriver + Cucumber suite now implements the workbook automation scope, mirrors/adapts the embedded legacy style, preserves TC traceability, and contains no PageFactory usage.

## Gate resolution

| Gate | Implemented resolution |
|---|---|
| Core implementation | `CREATED`: Maven dependencies, source, resources, features, hooks, runner, and reports. |
| TC16 | Executable acceptance assertion retained; current application fails by accepting whitespace. |
| TC17 | Partial approved-safe oracle: subtotal/item sum and total arithmetic; tax percentage remains unclaimed. |
| TC18 | Confirmation and empty-cart behavior implemented and passed; refresh/repeat submission remains out of scope. |
| TC19 | Cancel returns to Products without confirmation and preserves the cart; passed. |
| TC20 | Executable acceptance assertion retained; current application fails by completing an empty order. |
| TC21 | UI-state-only duplicate prevention implemented and passed. |
| TC23 | Chrome headless, 900px height, exact 1366/768/320 inner widths; objective usability checks passed, visual quality remains manual. |
| TC26 | Chrome 156 and Firefox 152 core flows passed. |

Conditional tags remain for traceability. TC16 and TC20 are not silently excluded from the default run.

## Implemented architecture

```text
src/test/java/saucedemo/
  config/
    TestConfig.java
  driver/
    SharedDriver.java
  pages/pom/
    LoginPage.java
    ProductsPage.java
    NavigationPage.java
    CartPage.java
    CheckoutInformationPage.java
    CheckoutOverviewPage.java
    CheckoutCompletePage.java
  steps/
    LoginSteps.java
    ProductSteps.java
    CartSteps.java
    CheckoutSteps.java
    AccessControlSteps.java
  steps/hooks/
    ScenarioHooks.java
    AuthenticatedHooks.java
  support/
    LoginActions.java
    PriceParser.java
  RunCucumberTest.java

src/test/resources/
  features/
    login/login.feature
    products/products.feature
    products/sorting.feature
    cart/cart.feature
    checkout/validation.feature
    checkout/order.feature
    security/access-control.feature
    compatibility/responsive.feature
    compatibility/browser.feature
  config.properties
```

The listed class split was implemented. A `ProductDetailsPage` was not created because no formal workbook test requires it.

## Implemented Maven and configuration

1. Java 21 was preserved as `maven.compiler.release`.
2. Selenium 4.51.0, Cucumber 8.0.4, JUnit 6.1.3, Jackson 2.22.4, Compiler 3.14.1, and Surefire 3.6.1 were resolved and compiled.
3. Surefire runs the JUnit Platform suite.
4. Runner annotations are the single glue/plugin configuration source; PicoContainer is discovered from its dependency.
5. Pretty, HTML, and JSON output is generated under `target/cucumber-reports/`, including screenshots.
6. `config.properties` defaults support system-property/environment overrides:
   - `base.url`
   - `browser.selection`
   - `browser.headless`
   - explicit-wait duration
   - approved test-user aliases/data
7. Selenium Manager resolves driver binaries; no local driver path is hard-coded.

## Page and step design

- Page classes use classic POM only.
- Element methods are static, accept `WebDriver sharedDriver`, and use descriptive legacy-style names.
- Prefer stable SauceDemo `data-test` selectors. Locator values are `NOT VERIFIED` until implementation.
- Page methods expose elements or typed values/lists; steps orchestrate workflows and assertions.
- Reuse generic product, cart, sorting, checkout-input, and validation steps.
- Parse money with `BigDecimal`; never compare formatted prices as strings or binary floating-point values.
- Copy displayed name/price lists before sorting to build an independent expected order.
- Use explicit waits for page headings, URL/state changes, error visibility, badge changes, and checkout transitions.
- Do not use arbitrary sleeps or long implicit waits.

## Implemented hook, login, logout, and screenshot lifecycle

1. `ScenarioHooks @Before` creates one driver per scenario.
2. `AuthenticatedHooks @Before("@authenticated")` logs in only after the driver exists.
3. TC01-TC04, TC25, and TC26 do not carry `@authenticated`.
4. TC05-TC21 normally carry `@authenticated`, subject to conditional-case filters.
5. TC23 is split by surface: login-page checks are not authenticated; protected-page checks may use `@authenticated`.
6. No hook performs logout.
7. One `ScenarioHooks @After` owns teardown:
   - if failed and the driver is active, capture screenshot bytes;
   - attach them with `scenario.attach(bytes, "image/png", scenarioName)`;
   - execute `quit()` afterward in `finally`;
   - clear the driver reference so repeated quit cannot occur;
   - do not silently suppress screenshot or quit failures.

This single teardown method was verified by PNG attachments for TC16 and TC20.

## Tag policy

| Tag | Purpose |
|---|---|
| `@TC01` ... `@TC26` | Workbook traceability. |
| `@P1`, `@P2` | Priority selection. |
| `@smoke` | Small deterministic P1 path, initially TC01 and an approved core order flow. |
| `@authenticated` | Tag-scoped pre-login only where login is not under test. |
| `@conditional` | Requirement-dependent tests excluded until their gate is approved. |
| `@responsive` | TC23 execution profile. |
| `@browser_matrix` | TC26 execution profile. |
| `@manual` | Reserved for documentation; TC22/TC24 have no automated scenario. |

Do not use an inverse tag such as `@noLogin` to compensate for a universal login hook. Absence of `@authenticated` is the safe default.

## Completed implementation sequence

1. **Foundation:** POM, runner, configuration, and dependency compilation completed.
2. **Driver/config:** central lifecycle, browser selection, exact Chromium viewport emulation, waits, and safe quit completed.
3. **Hooks/evidence:** tag-scoped login and screenshot-before-quit verified.
4. **Functional scope:** TC01-TC21 implemented.
5. **Responsive/access/browser scope:** TC23, TC25, and TC26 implemented and executed.
6. **Manual integrity:** TC22, TC24, and exploratory charter retained as manual.
7. **Review:** prohibited APIs, duplicate expressions, driver paths, sleeps, lifecycle conflicts, and TC placement checked with no violations.

## Actual validation

1. `mvn -DskipTests test-compile`: passed.
2. TC01 targeted Chrome run: passed after adding Cucumber 8's required Jackson provider.
3. Deterministic Chrome suite: 19/19 examples passed.
4. Conditional suite: TC17-TC19 and TC21 passed; TC16/TC20 failed with screenshots.
5. TC23: 1366x900, 768x900, and 320x900 passed.
6. TC26: passed in Chrome and Firefox.
7. Complete Chrome suite: 27/29 passed; only TC16/TC20 failed.
8. Clean baseline excluding TC16/TC20: 27/27 passed.
9. Reports updated with commands, artifacts, failures, and limitations.

## Completion criteria

- All 24 automatable IDs map to scenarios and execution evidence.
- Manual and partial scope is reported honestly.
- No PageFactory or arbitrary sleep exists.
- Authenticated setup is tag-scoped.
- Negative login, logout, direct URL, responsive full-flow, and browser full-flow scenarios are not pre-authenticated.
- Failure screenshot attachment happens before the only driver quit path.
- Compilation, Chrome core/responsive runs, and Chrome/Firefox TC26 runs succeeded.
- TC16 and TC20 remain explicit application/requirement failures.
