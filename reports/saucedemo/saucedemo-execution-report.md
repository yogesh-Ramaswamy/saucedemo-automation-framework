# SauceDemo Execution Report

**Status:** `EXECUTED` - 27 passed, 2 failed in the complete Chrome run  
**Execution date:** 2026-10-29

## Environment

| Component | Version/result |
|---|---|
| OS | Windows Server 2019, x64 |
| Java | Azul Zulu OpenJDK 21.0.10 LTS |
| Maven | Apache Maven 3.8.1 |
| Chrome | 156.0.8078.48 |
| Firefox | 152.0.5 |
| Edge | 156.0.4314.50, detected but not required for the workbook matrix |
| Selenium | 4.51.0 |
| Cucumber | 8.0.4 |
| JUnit | 6.1.3 |

`JAVA_HOME` was set to `C:\Program Files\Java\jdk21` for each Maven command because the ambient value was invalid.

## Commands and results

| Command/selection | Result |
|---|---|
| `mvn -q -DskipTests test-compile` | `EXECUTED`: passed after foundation creation and after complete glue creation. |
| `mvn test -Dcucumber.filter.tags=@TC01 -Dbrowser.headless=true` | Initial discovery failed because Cucumber 8 reporting required Jackson. Jackson Databind/JDK8 modules were added. |
| `mvn -q test -Dcucumber.filter.tags=@TC01 -Dbrowser.headless=true` | `EXECUTED`: TC01 passed in Chrome. |
| Deterministic filter: `not @conditional and not @responsive and not @browser_matrix` | First run: 16 examples passed; three TC25 examples errored because the menu image, rather than its button, was selected. Locator fixed to `react-burger-menu-btn`. |
| `mvn -q test -Dcucumber.filter.tags=@TC25 -Dbrowser.headless=true` | `EXECUTED`: all three protected URL examples passed. |
| Deterministic filter rerun | `EXECUTED`: all 19 examples passed, covering TC01-TC15 and TC25. |
| `mvn -q test -Dcucumber.filter.tags=@conditional -Dbrowser.headless=true` | `EXECUTED`: TC17, TC18, TC19, and TC21 passed; TC16 and TC20 failed against workbook assumptions. |
| `mvn -q test -Dcucumber.filter.tags=@TC23 -Dbrowser.headless=true` | Initial run passed 1366px and 768px; 320px was blocked by Chrome's 512px minimum window. Chromium device metrics were added. |
| TC23 rerun | `EXECUTED`: all three exact inner viewports (1366x900, 768x900, 320x900) passed login, products, cart, checkout, clipping, and horizontal-overflow checks. |
| TC26 with `-Dbrowser.selection=chrome` | `EXECUTED`: core shopping flow passed in Chrome. |
| TC26 with `-Dbrowser.selection=firefox` | `EXECUTED`: core shopping flow passed in Firefox. |
| Complete Chrome run: `mvn -q test -Dbrowser.selection=chrome -Dbrowser.headless=true` | `EXECUTED`: 29 examples, 27 passed, 2 failed, 0 skipped. |
| Passing baseline: `not @TC16 and not @TC20` | `EXECUTED`: 27 examples passed, 0 failed. Surefire reported 29 discovered tests with 2 filtered/skipped. |

## Complete Chrome result

| Status | TC IDs |
|---|---|
| Passed | TC01-TC15, TC17-TC19, TC21, TC23, TC25, TC26 |
| Failed | TC16, TC20 |
| Manual / not automated | TC22, TC24 |

Scenario Outline expansion produced 29 examples:

- TC04: 2 required-field examples
- TC23: 3 viewport examples
- TC25: 3 protected-path examples
- All other automated TCs: 1 example each

## Verified failures

| TC | Observed result | Workbook expectation |
|---|---|---|
| TC16 | Whitespace-only checkout fields were accepted and the application navigated to Checkout Overview. | Whitespace should not satisfy required fields. |
| TC20 | An empty cart proceeded through customer details, Finish, and order confirmation. | A zero-item order should be prevented. |

Both failures are reproducible application/requirement mismatches. Neither was suppressed or converted into a passing characterization.

## Failure evidence verification

The preserved full-run JSON contains:

- one `image/png` attachment for TC16;
- one `image/png` attachment for TC20;
- attachments stored on the `after` hook result;
- screenshot capture completed before driver teardown.

This verifies the required failed-screenshot ordering with actual failures.

## Generated artifacts

| Artifact | Purpose |
|---|---|
| `target/cucumber-reports/full-chrome.html` | Preserved complete Chrome run with 27 pass / 2 fail and embedded screenshots. |
| `target/cucumber-reports/full-chrome.json` | Machine-readable complete Chrome evidence. |
| `target/cucumber-reports/accepted-chrome.html` | Passing 27-example baseline excluding TC16/TC20. |
| `target/cucumber-reports/accepted-chrome.json` | Machine-readable passing baseline. |
| `target/cucumber-reports/tc26-chrome.html/json` | Preserved Chrome browser-matrix evidence. |
| `target/cucumber-reports/tc26-firefox.html/json` | Preserved Firefox browser-matrix evidence. |
| `target/surefire-reports/` | Maven/JUnit Platform summaries and failures. |

## Warnings and limitations

1. Chrome emitted a non-fatal warning that CDP 156 used the nearest bundled CDP 155 implementation. Selenium 4.51.0 was the latest Maven Central release checked on the execution date.
2. TC17 independently verifies item sum, displayed subtotal, non-negative tax, and total arithmetic. It does not verify an unspecified tax percentage.
3. TC23 automates control visibility, operation, clipping, and horizontal overflow. It does not replace manual visual-quality review.
4. TC22 usability, TC24 keyboard/accessibility, and the `problem_user` exploratory charter remain manual.
5. A default unfiltered `mvn test` correctly returns failure while TC16 and TC20 remain unresolved.

## Execution decision

The framework and all non-blocked automated scenarios are operational. The clean executable baseline is 27/27 passing examples when TC16 and TC20 are excluded explicitly; the honest complete result remains 27/29 with those two observed application failures.
