# Legacy Project Reference Profile — Extracted from `src(9).zip`

**Purpose:** This file embeds the relevant conventions from the user's legacy source ZIP so the coding agent on the user's machine does not need that ZIP. The original archive was inspected in the ChatGPT workspace; the target machine does not need the archive.

## 1. Repository layout observed

- Java test source root: `src/test/java/`
- Feature root: `src/test/resources/features/`
- Shared framework package: `eqtest.shared`
- Example page package: `eqtest.alphaApi.pages.pom`
- Example step package: `eqtest.alphaApi.steps`
- Example hook package: `eqtest.selftradeClassic.steps.hooks`
- Other hooks exist under product-specific `steps/hooks` packages.
- Runner: `src/test/java/eqtest/RunnerTest.java`
- Cucumber config: `src/test/resources/cucumber.properties`
- Shared scenario context: `src/test/java/eqtest/CucumberContext.java`

## 2. POM/page-object conventions

Observed in `src/test/java/eqtest/alphaApi/pages/pom/Login.java` and `DashBoard.java`:
- Package declaration uses a feature/application namespace ending in `.pages.pom`.
- Page classes are named by page/area, e.g. `Login`, `DashBoard`.
- Many page methods are `public static`.
- Methods accept the driver as an explicit argument named `sharedDriver`, e.g. `public static WebElement field_UserName(WebDriver sharedDriver)`.
- Method names describe element role and purpose, e.g. `field_UserName`, `button_PreLogin`, `button_Deal`, `button_FindInvestments`.
- Methods often locate elements inside the method with `sharedDriver.findElement(By.id(...))`, `By.xpath(...)`, `By.className(...)`, `By.linkText(...)`.
- Methods return `WebElement`, `List<WebElement>`, `String`, `By`, or other typed values where useful.
- Page classes import Selenium `By`, `WebDriver`, `WebElement`; some also use `ExpectedConditions`, `Select`, or JavascriptExecutor as needed.
- Step definitions call page methods and handle workflow/assertions.
- Some unrelated legacy classes use PageFactory. **Do not copy that aspect.** User explicitly requires conventional POM without PageFactory.

### Recommended SauceDemo adaptation

Use a package such as `saucedemo.pages.pom` and static methods like:
- `field_UserName(WebDriver sharedDriver)`
- `field_Password(WebDriver sharedDriver)`
- `button_Login(WebDriver sharedDriver)`
- `label_LoginError(WebDriver sharedDriver)`
- `label_ProductsTitle(WebDriver sharedDriver)`
- `button_AddToCart(WebDriver sharedDriver, String productTestId)`
- `button_RemoveFromCart(WebDriver sharedDriver, String productTestId)`
- `dropdown_SortProducts(WebDriver sharedDriver)`
- `label_CartBadge(WebDriver sharedDriver)`
- checkout field/button/value methods grouped into relevant page classes.

Prefer `By.cssSelector("[data-test='...']")` or stable IDs. Avoid brittle positional XPath. Keep page methods small and reusable. If return type should be `By` or value rather than `WebElement`, preserve the descriptive naming convention while choosing a clean type.

## 3. Step-definition conventions

Observed in `src/test/java/eqtest/alphaApi/steps/Alpha_Holdings_steps.java`:
- Package ends in `.steps`.
- Class names describe the feature, e.g. `Alpha_Holdings_steps`.
- Constructor injection is used for shared dependencies, e.g. `Alpha_Holdings_steps(SharedDriver sharedDriver, StaticDataProvider staticDataProvider)`.
- Cucumber annotations come from `io.cucumber.java.Before`, `io.cucumber.java.en.Then`, `io.cucumber.java.en.When`.
- Steps call page methods with `sharedDriver`, use `WebDriverWait`/`FluentWait` and Selenium `ExpectedConditions`.
- Assertions commonly use `org.junit.Assert`.
- Some legacy steps use regular-expression annotation strings such as `@When("^navigate to Holdings ...$")`; for new SauceDemo work use readable Cucumber expressions if the chosen Cucumber version supports them, while preserving the project's general naming/annotation style.

## 4. Hooks and scenario tags

Observed in:
- `src/test/java/eqtest/selftradeClassic/steps/hooks/selftradeClassic.java`
- `src/test/java/eqtest/saga/steps/hooks/SAGA.java`
- `src/test/java/eqtest/EQI/steps/hooks/selftrade.java`

Conventions:
- Hooks live in a distinct `steps.hooks` package.
- Cucumber hook imports use `io.cucumber.java.Before`, `io.cucumber.java.After`, and `io.cucumber.java.Scenario`.
- Hook classes often use constructor injection.
- Setup is frequently tag-specific, e.g. `@Before("@SelftradeClassicLogin")`, `@Before("@SagaLogin")`, rather than logging in every scenario.
- `@After` can be tag-scoped for selected teardown behavior; one example uses `@After("@CloseDriver")`.
- The archive contains many app-specific hooks; do not copy those business actions into SauceDemo.

### SauceDemo hook policy
- Use a driver lifecycle hook for starting and quitting the browser once per scenario, matching target runner capabilities.
- Login should be a tagged setup only for scenarios that require an authenticated session. Login-invalid, locked-out, empty-credential, direct-access, and logout tests must not be pre-logged in.
- Failure screenshot must happen in an `@After` hook before `quit()`.
- Explicit logout is a tested business scenario, not a universal cleanup action.
- Ensure one driver teardown path; do not create conflicting `@After` methods that both quit the same session.

## 5. Shared driver conventions

Observed in `src/test/java/eqtest/shared/SharedDriver.java`:
- `SharedDriver` implements `WebDriver`, `JavascriptExecutor`, `TakesScreenshot`, and `Interactive`.
- It creates a real driver lazily and wraps it in Selenium `EventFiringDecorator`.
- Browser choice is read from `System.getProperty("browser.selection")`; supported branches include Firefox, IE, Chrome, and Edge in that legacy environment.
- It applies implicit wait, page-load timeout, and script timeout.
- A JVM shutdown hook also quits the real driver.
- The class provides `getScreenshotAs(...)` through `TakesScreenshot`.
- The original driver is designed for a larger existing framework; it has environment-specific capabilities and browser setup. Do not copy it wholesale. For SauceDemo implement a smaller `SharedDriver` or `DriverManager` that keeps browser creation centralized and lifecycle deterministic. Browser property support (e.g. `-Dbrowser.selection=chrome`) is useful if compatible with installed drivers.
- Avoid a duplicate JVM shutdown hook plus per-scenario quit that can cause lifecycle ambiguity.

## 6. Screenshot and reporting evidence

Search of the legacy Java archive found screenshot attachment-related code in:
- `src/test/java/eqtest/shared/SharedDriver.java` — `getScreenshotAs`
- `src/test/java/eqtest/alphaApi/steps/api_steps.java` — `getScreenshotAs` and `scenario.attach`
- `src/test/java/eqtest/EQI/steps/ui_steps.java` — `scenario.attach`
- `src/test/java/eqtest/xanite/steps/xanite_steps.java` — `scenario.attach`

The sampled hook files did not establish a single universal failure screenshot hook. Therefore SauceDemo should implement an explicit, reliable one:
1. In `@After`, check `scenario.isFailed()`.
2. Capture screenshot bytes from the active driver.
3. Call `scenario.attach(bytes, "image/png", scenario.getName())` (adapt to actual Cucumber API version).
4. Only then quit the driver.
5. Keep screenshot evidence available in Cucumber report output where supported.

Do not claim the legacy project universally attached screenshots on every failure; this profile distinguishes observed APIs from the recommended SauceDemo implementation.

## 7. Runner and Cucumber configuration

Observed `src/test/java/eqtest/RunnerTest.java`:
- JUnit Platform suite annotations: `@Suite`, `@IncludeEngines("cucumber")`, `@SelectClasspathResource`.
- Uses `@ConfigurationParameter` with Cucumber `GLUE_PROPERTY_NAME` and `PLUGIN_PROPERTY_NAME`.
- Selects a feature resource under `features/`.
- Plugins include `pretty`, `io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm`, and `listeners.AllureReportGenerator` in the original larger project.
- Glue packages are explicitly listed.

Observed `src/test/resources/cucumber.properties`:
- `cucumber.glue=...`
- `cucumber.plugin=pretty,io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm`
- `cucumber.object-factory=io.cucumber.picocontainer.PicoFactory`

For SauceDemo:
- Use a compatible JUnit Platform Cucumber runner if dependencies permit.
- Choose one consistent source of glue/plugin configuration to avoid conflicting settings.
- Use Cucumber PicoContainer only if the selected dependency is included and constructor injection is used.
- Do not add Allure or custom listeners merely to mimic names if their dependencies are absent; add reporting plugins only when needed and compatible.
- Keep runner package/glue consistent with the new SauceDemo package.

## 8. Scenario context and configuration

Observed `src/test/java/eqtest/CucumberContext.java`:
- A scenario context class uses `Map<String,Object>`.
- Provides `setContext(String,Object)`, `getContext(String)`, `isContains(String)`, and typed getter helpers.
- For SauceDemo, only add a context object if scenarios need to share state across glue classes; avoid overengineering.

Observed `StaticDataProvider`:
- Reads JSON configuration and uses system properties such as `-Denvironment`.
- Has static parameter helpers.
- For SauceDemo, a simple properties/config approach is sufficient; support `-Dbrowser.selection=chrome` if practical and use a base URL property/environment variable.
- Do not log passwords or secrets.

## 9. Waits and assertions

- Legacy step examples use `WebDriverWait` or `FluentWait`, `ExpectedConditions`, and JUnit `Assert`.
- Use explicit waits for SauceDemo state transitions and avoid arbitrary `Thread.sleep`.
- Prefer stable `data-test` selectors and assert user-visible outcomes.

## 10. Current target snapshot facts

The supplied `saucedemo.zip` contains:
- `saucedemo/pom.xml`
- `saucedemo/src/main/java/PlaywrightProject/Main.java`
- empty `saucedemo/src/test/java/`
- empty `saucedemo/src/test/resources/`
- no `.github` folder in that ZIP.

The target is a starter Maven project, not an existing Selenium/Cucumber framework. Agent should add the minimal harness needed, keeping the Java 21 configuration if still present in the active workspace.

## 11. Boundaries

- This profile is a distilled style guide, not a complete copy of the old source tree.
- The original legacy archive is not available on the user's machine.
- Do not claim to have searched that machine, imported old methods, or reused its runtime.
- Describe implementation as “style mirrored/adapted from the embedded reference profile.”
