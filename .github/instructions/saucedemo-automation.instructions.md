# SauceDemo implementation instructions

- Do not require the legacy ZIP on the user's machine. Its style has been extracted into `.github/reference/saucedemo-legacy-reference-profile.md`.
- The current target is the SauceDemo Maven project.
- Match the legacy profile: page classes under `pages.pom`, static page methods receiving `WebDriver sharedDriver`, descriptive names like `field_...`/`button_...`, separate step definitions and hooks, Cucumber `io.cucumber.java.*`, JUnit assertions, explicit Selenium waits, feature files under `src/test/resources/features/`, JUnit Platform Cucumber runner/configuration where compatible.
- Adapt legacy patterns to SauceDemo. Do not copy old app-specific code or environment-specific browser paths.
- Classic POM only. Never use PageFactory, `@FindBy`, `PageFactory.initElements`, or `AjaxElementLocatorFactory`.
- Inspect current code before creating anything and reuse current-target assets first.
- Tag-scope login setup. Negative login, direct URL, and logout scenarios must not be pre-authenticated.
- On failure, capture and attach screenshot before driver teardown. Do not logout/clean up before evidence capture.
- Driver setup/teardown must occur once, safely.
- Keep TC IDs traceable and manual-only scope honest.
- Report exact files/patterns searched, reused/created/modified files, commands run, results, and limitations.
