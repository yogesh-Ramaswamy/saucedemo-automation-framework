# SauceDemo Legacy Reference Profile Note

**Status:** `REFERENCE PROFILE` with `MIRRORED/ADAPTED` implementation  
**Review date:** 2026-10-29  
**Canonical source:** [`.github/reference/saucedemo-legacy-reference-profile.md`](../../.github/reference/saucedemo-legacy-reference-profile.md)

This report points to the canonical embedded profile supplied with the project. It does not claim that the original legacy ZIP was present or inspected on this machine, and it does not add findings beyond that profile.

## Applicable reference conventions

| Area | Reference convention to mirror or adapt |
|---|---|
| Page objects | Use a package ending in `pages.pom`, classic `By`/`WebDriver` lookups, static methods that accept `WebDriver sharedDriver`, and descriptive names such as `field_...`, `button_...`, and `label_...`. |
| PageFactory | Prohibited. Do not use `PageFactory`, `@FindBy`, `PageFactory.initElements`, or `AjaxElementLocatorFactory`. |
| Steps | Keep Cucumber step definitions in a separate `steps` package, call page methods with the shared driver, use `io.cucumber.java.*`, explicit waits, and JUnit assertions. |
| Hooks | Keep hooks in `steps.hooks`; create one deterministic per-scenario driver lifecycle and tag-scope authenticated setup. |
| Login | Pre-authenticate only scenarios tagged for authenticated setup. Negative login, direct URL, logout, and full-flow scenarios must perform their own login behavior. |
| Logout | Treat logout as tested business behavior, not universal cleanup. |
| Failure evidence | In the failure path, capture `OutputType.BYTES`, attach with `Scenario.attach`, and only then quit the driver. |
| Driver | Centralize driver creation; support a browser system property where practical; do not copy legacy environment-specific paths or duplicate shutdown mechanisms. |
| Runner | Use a compatible JUnit Platform Cucumber runner and one source of glue/plugin configuration. |
| Features | Store feature files under `src/test/resources/features/` and preserve workbook TC IDs. |
| Reporting | Keep generated HTML/JSON capable of carrying screenshot attachments and report execution facts honestly. |

## Current target comparison

- `CURRENT TARGET FOUND`: Java 21 Maven project with an implemented Selenium/Cucumber test framework.
- `CURRENT TARGET FOUND`: `.github` agent, instruction, prompt, and canonical reference files.
- `CREATED`: classic static page objects, reusable step definitions, tag-scoped hooks, shared driver, JUnit Platform runner, configuration, and nine feature files.
- `MIRRORED/ADAPTED`: conventions listed above, including `pages.pom`, `WebDriver sharedDriver`, element-role naming, and screenshot attachment.
- `REUSED`: no legacy implementation method or class was copied; Java 21, workbook traceability, and embedded conventions were reused.
- `EXECUTED`: all 24 automatable TC IDs in Chrome, plus TC26 in Firefox.
- `NOT RUN`: TC22 and TC24 automation because the workbook designates them manual.

## Boundary

The canonical profile is a style and evidence summary, not a runtime dependency. The implementation uses the current target and workbook as its functional source and describes legacy alignment as "mirrored/adapted from the embedded reference profile."
