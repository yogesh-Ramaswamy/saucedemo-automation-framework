# SauceDemo Test Design Review

**Status:** `IMPLEMENTED` and `EXECUTED`; two requirement failures remain  
**Review date:** 2026-10-29

## Workbook integrity

- Three visible sheets were read in full.
- `Test Cases` contains 26 unique, sequential IDs from TC01 through TC26.
- Priority distribution: 12 P1, 14 P2, 0 P3.
- Workbook automation designation: 24 Yes, 2 No.
- `Questions & Assumptions` contains Q1 through Q10 without missing or duplicate IDs.
- There are no formulas, hidden rows, or hidden columns.
- TC IDs and manual scope are suitable traceability anchors.

## Post-implementation execution classification

| Result | IDs | Evidence |
|---|---|---|
| Passed in the complete Chrome run | TC01-TC15, TC17-TC19, TC21, TC23, TC25, TC26 | 27 passing examples after Scenario Outline expansion. |
| Failed against workbook expectation | TC16, TC20 | Whitespace accepted; empty order completed. Each failure has a PNG attachment. |
| Also passed in Firefox | TC26 | Full login-to-confirmation flow. |
| Manual by workbook design | TC22, TC24 | No automated scenario or coverage claim. |

TC17 has partial business-oracle coverage: item sum, subtotal, displayed tax non-negativity, and total arithmetic are verified; an unspecified tax percentage is not.

## Pre-implementation automation-readiness classification

This retained classification explains how the implementation scope was chosen.

| Class | IDs | Count |
|---|---|---:|
| Ready for initial automation design | TC01, TC06-TC15, TC25 | 12 |
| Needs test-data or assertion-oracle refinement | TC02-TC05 | 4 |
| Needs Product Owner decision or partial-scope agreement | TC16-TC21 | 6 |
| Hybrid/environment-dependent and cannot be claimed without evidence | TC23, TC26 | 2 |
| Manual as designed | TC22, TC24 | 2 |

## Test-case findings

The findings below retain the original design review. Actual execution results above supersede `NOT VERIFIED` behavior.

| ID | Review classification | Finding and implementation implication |
|---|---|---|
| TC01 | Ready | Objective smoke flow. Must not use the authenticated login hook because login is the behavior under test. Assert both protected URL/state and Products heading. |
| TC02 | Refine oracle | Objective rejection behavior, but "appropriate error" is not exact. Fix accepted error text or semantic assertion before coding. No pre-authentication. |
| TC03 | Refine oracle | Objective locked-user behavior, but the exact locked-out message is not supplied. No pre-authentication. |
| TC04 | Refine/split | Contains two cases: both fields empty and password empty after username entry. Preserve `@TC04` while using separate examples/subscenarios so failures identify the missing field. |
| TC05 | Refine data oracle | Structural visibility is automatable, including non-empty text, parseable prices, loaded images, and controls. Exact expected catalog names/descriptions/prices are absent, so do not claim content correctness without an approved baseline. |
| TC06 | Ready | Compare displayed names with an independently sorted copy in ascending order. Do not hard-code current UI order as the oracle. |
| TC07 | Ready | Same reusable name comparator in descending order. Undefined tie ordering must not fail the test. |
| TC08 | Ready | Strip currency formatting and compare numeric decimal values ascending. |
| TC09 | Ready | Reuse numeric parsing and compare descending. |
| TC10 | Ready with explicit data | Replace "first" and "second" with two named/stable products so the scenario does not depend on current sort order. Verify badge after each action and cart contents. |
| TC11 | Ready with explicit data | Name the product in setup and action. Verify both badge state and absence from cart. |
| TC12 | Ready with explicit data | Name both initial products and the removed product. Verify the retained product, removed product, and badge. |
| TC13 | Ready | Required first-name validation is objective. Record the accepted field-specific message in scenario data before implementation. |
| TC14 | Ready | Reuse checkout input and validation steps while keeping a distinct TC tag. |
| TC15 | Ready | Reuse checkout input and validation steps; postal-format rules beyond non-empty remain out of scope under Q3. |
| TC16 | PO decision | Expected whitespace rejection is explicitly an assumption. Automating it as a required pass/fail outcome would encode an unapproved rule. It may be a characterization test until clarified. |
| TC17 | Partial/PO decision | Subtotal sum and `total = subtotal + displayed tax` are objective. Correctness of the tax value itself cannot be independently verified until Q2 defines rate, base, and rounding. |
| TC18 | Partial/PO decision | Confirmation visibility is objective. Required post-finish cart state, refresh behavior, and duplicate submission behavior depend on Q9. |
| TC19 | PO decision | "Previous page" is ambiguous and cart preservation is explicitly undefined. Q6 must define exact destination and cart state. "No confirmation" can be asserted now. |
| TC20 | PO decision | The expected prevention of zero-item checkout is an explicit assumption. Do not encode it as an acceptance oracle until Q4 is answered; current live behavior was not tested. |
| TC21 | Partial/PO decision | The UI-state check that Add changes to Remove and the badge remains consistent is objective. Quantity/duplicate semantics require Q5. Avoid bypassing the UI merely to force a duplicate. |
| TC22 | Manual | Clarity and ease of recovery require human judgment. Existing objective error visibility and recovery checks may support it, but must not be reported as full TC22 automation. |
| TC23 | Hybrid | Functional visibility and overflow checks can be automated, but visual quality remains manual. Define viewport height, headless/headed mode, browser, device scale, and acceptable overflow. Confirm actual inner width during execution. |
| TC24 | Manual | Keyboard reachability can be sampled automatically, but sensible order, visible focus quality, and absence of usability barriers require manual evaluation. Do not claim full automation. |
| TC25 | Ready with split scenarios | Separate logout behavior from logged-out direct access examples for inventory, cart, and checkout. Do not use the authenticated hook; scenario steps must establish login, log out, then navigate directly. |
| TC26 | Environment-dependent | Requires an approved browser/version matrix and available drivers/runners. Login is part of the flow, so do not pre-authenticate. Coverage exists only after each matrix entry executes. |

## Exploratory Charter review

The `Exploratory Charter` sheet is well scoped as a 15-minute manual investigation of `problem_user`, with optional comparison to `standard_user`. It covers product images, sorting, product navigation/details, cart behavior, and limited checkout exploration.

- Keep it manual and timeboxed; do not convert it into a fixed pass/fail feature.
- Record page, steps, expected vs actual, severity, and evidence only for observed findings.
- Do not assume the known-problem user will expose a particular defect.
- Product-detail navigation is present here, but not as a formal TC01-TC26 case. The implementation plan therefore does not claim a formal product-detail automated test.
- If an exploratory issue is reproducible and approved for regression, create a separate formal test requirement before automating it.

## Questions and assumptions impact

| ID | Review impact |
|---|---|
| Q1 Registration | Registration is consistently out of scope. No implementation impact. |
| Q2 Tax calculation | Blocks an independent tax-value oracle for TC17. Subtotal and displayed arithmetic can proceed. |
| Q3 Postal code | TC15 can cover empty required input. Additional format/length/character tests remain out of scope until defined. |
| Q4 Empty cart | Blocks the required outcome for TC20. |
| Q5 Duplicate products | Blocks quantity/duplicate expectations for TC21 and affects badge/subtotal semantics. |
| Q6 Cancel behavior | Blocks exact destination and cart-preservation assertions for TC19. |
| Q7 Sorting | Primary sorting checks can proceed. Do not assert secondary tie order or persistence. |
| Q8 Cart badge | Unique-product add/remove checks can proceed using the stated assumption; duplicate semantics still depend on Q5. |
| Q9 Order confirmation | Core confirmation can proceed; post-finish cart, refresh, and repeated submission need agreement. |
| Q10 Access control | Sufficient security invariant for TC25: protected content must be unavailable. Redirect versus access-denied presentation can remain flexible unless exact UX is required. |

## Resolved and remaining design gaps

1. Exact current SauceDemo error text is encoded for TC02-TC04 and TC13-TC15 and passed.
2. TC05 verifies structural completeness, loaded images, positive parseable prices, and available controls; no unapproved exact catalog baseline was invented.
3. Q2 remains open, so TC17 does not claim tax-rate verification.
4. Q4 is contradicted by execution: TC20 reaches confirmation with an empty cart.
5. Q5 objective UI behavior passed: Add changes to Remove and one cart item remains.
6. Q6/Q9 implemented behavior passed: Cancel returns to Products with cart preserved; Finish confirms and empties the cart.
7. TC26 passed in the workbook-suggested Chrome/Firefox matrix.
8. TC23 passed at 1366x900, 768x900, and 320x900 inner viewports; qualitative visual review remains manual.
9. Named products replaced positional first/second references.
10. TC04, TC23, and TC25 use traceable Scenario Outlines while retaining the official TC tag.
11. TC22, TC24, and the exploratory charter remain outside automated coverage metrics.

## Review decision

The workbook produced complete automated traceability for its 24 automatable IDs. The framework has a 27-example passing baseline when TC16 and TC20 are explicitly excluded. The honest unfiltered result is 27/29 because current SauceDemo behavior contradicts those two workbook assumptions. Manual and partial coverage boundaries remain explicit.
