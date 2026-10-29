# SauceDemo Reuse Matrix

**Status:** `REUSED`, `CREATED`, and `MIRRORED/ADAPTED` implementation  
**Review date:** 2026-10-29

## Reuse conclusion

No automation implementation existed at the baseline. The completed framework reuses the Java 21 project, workbook traceability/data, and embedded profile conventions. No legacy method is claimed as `REUSED`; created code is `MIRRORED/ADAPTED` from the reference conventions.

## Existing asset matrix

| Asset | Status now | Proposed disposition after approval |
|---|---|---|
| Java 21 compiler level in `pom.xml` | `REUSED` | Preserved as `maven.compiler.release=21`. |
| Maven coordinates | `REUSED` | Preserved as `PlaywrightProject:saucedemo:1.0-SNAPSHOT`. |
| `Main.java` starter | `CURRENT TARGET FOUND` | Left untouched; it has no test-framework reuse value. |
| Workbook IDs `TC01`-`TC26` | `REUSED` | Exact tags created for every automatable case; TC22/TC24 remain manual. |
| Workbook test data | `REUSED` | Named products, credentials, validation inputs, viewports, and protected paths carried into scenarios/config. |
| Workbook manual scope | `REUSED` | TC22, TC24, and the exploratory charter remain manual and excluded from automation claims. |
| `.github` legacy profile | `REFERENCE PROFILE` | `MIRRORED/ADAPTED`; no unavailable legacy runtime code copied. |
| Current page methods | `CREATED` | Seven classic static POM classes under `saucedemo.pages.pom`. |
| Current steps | `CREATED` | Six classes with 48 unique Cucumber expressions. |
| Current hooks/driver | `CREATED` | Deterministic scenario lifecycle, tagged login, screenshots, and browser selection. |
| Current runner/config | `CREATED` | One annotation-based JUnit Platform runner plus `config.properties`. |
| Current features | `CREATED` | Nine feature files under `src/test/resources/features/`. |

## Implemented reusable page-method catalog

These methods are implemented. Stable `data-test` locators and the menu button ID were verified in browser execution.

| Planned class | Representative reusable methods | Workbook coverage |
|---|---|---|
| `LoginPage` | `field_UserName(WebDriver sharedDriver)`, `field_Password(...)`, `button_Login(...)`, `label_LoginError(...)` | TC01-TC04, TC25, TC26 |
| `ProductsPage` | `label_ProductsTitle(...)`, `labels_ProductNames(...)`, `labels_ProductDescriptions(...)`, `labels_ProductPrices(...)`, `images_Products(...)`, `dropdown_SortProducts(...)`, `button_AddToCart(..., productId)`, `button_RemoveFromProducts(..., productId)` | TC01, TC05-TC11, TC21, TC23, TC26 |
| `NavigationPage` | `link_Cart(...)`, `label_CartBadge(...)`, `button_Menu(...)`, `link_Logout(...)` | TC10-TC12, TC18-TC21, TC25-TC26 |
| `CartPage` | `labels_CartItemNames(...)`, `labels_CartItemPrices(...)`, `button_RemoveFromCart(..., productId)`, `button_Checkout(...)` | TC10-TC12, TC17-TC21, TC23, TC25-TC26 |
| `CheckoutInformationPage` | `field_FirstName(...)`, `field_LastName(...)`, `field_PostalCode(...)`, `button_Continue(...)`, `button_Cancel(...)`, `label_CheckoutError(...)` | TC13-TC17, TC20, TC23, TC25-TC26 |
| `CheckoutOverviewPage` | `labels_OverviewItemNames(...)`, `labels_OverviewItemPrices(...)`, `label_ItemSubtotal(...)`, `label_Tax(...)`, `label_Total(...)`, `button_Finish(...)`, `button_Cancel(...)` | TC17-TC20, TC26 |
| `CheckoutCompletePage` | `label_ConfirmationHeader(...)`, `label_ConfirmationText(...)`, `button_BackHome(...)` | TC18, TC26 |

No formal product-detail test exists in the `Test Cases` sheet. Product-detail navigation appears only in the exploratory charter, so no unnecessary `ProductDetailsPage` was created.

## Implemented reusable step catalog

The responsibilities below are implemented as parameterized Cucumber steps.

| Step responsibility | Reuse approach |
|---|---|
| Open login page | One `Given` step for non-authenticated login and access-control scenarios. |
| Submit credentials | One parameterized `When` step accepting username/password aliases or values. |
| Verify login result/error | Shared product-page assertion and parameterized login-error assertion. |
| Select product sort | One parameterized step for all four options. |
| Verify sorted names/prices | Two typed comparison steps; parse prices numerically rather than lexically. |
| Add/remove named product | Parameterized product action steps using stable product IDs, reused across Products and Cart scenarios. |
| Verify cart badge/content | Parameterized count, contains, and absent assertions. |
| Reach checkout with products | Shared setup step or scenario background for authenticated checkout scenarios; not used for login/logout tests. |
| Enter checkout details | One parameterized step with explicit empty/whitespace values. |
| Verify checkout validation | Parameterized expected field/error semantics. |
| Verify overview arithmetic | Shared price parsing and monetary comparison with exact decimal handling. |
| Finish/cancel checkout | Separate business-action steps with explicit outcome assertions. |
| Log out/open protected URL | Scenario-owned steps for TC25; never a universal hook. |
| Change viewport/browser | Profile/support helpers for TC23/TC26; execution evidence required before coverage is claimed. |

Step definitions call page methods and support utilities. Hooks do not invoke step-definition methods. `LoginActions` is shared beneath authenticated setup and explicit login steps.

## Hook and lifecycle reuse matrix

| Concern | Current target | Planned design |
|---|---|---|
| Driver creation | `CREATED` | One central `SharedDriver`; Chrome/Firefox/Edge plus `-Dbrowser.selection`. |
| Scenario setup | `CREATED` | One untagged driver-start `@Before(order=0)`. |
| Authenticated setup | `CREATED` | `@Before("@authenticated", order=10)` executes after driver start. |
| Negative login | `CREATED` | TC02-TC04 omit `@authenticated`. |
| Valid login test | `CREATED` | TC01 performs login in the scenario. |
| Logout/direct URL | `CREATED` | TC25 logs in, logs out, and opens each protected path within the outline. |
| Full browser flow | `CREATED` | TC26 performs login and checkout in Chrome and Firefox. |
| Failure screenshot | `EXECUTED` | One `@After` attached PNG evidence for TC16 and TC20 before quit. |
| Logout cleanup | `NOT CREATED` | Correctly omitted. |
| Driver quit | `CREATED` | Exactly one hook-owned quit path; safe if setup did not complete. |

## Implemented feature-to-ID reuse

| Planned feature group | IDs |
|---|---|
| `login/login.feature` | TC01-TC04 |
| `products/products.feature` | TC05 |
| `products/sorting.feature` | TC06-TC09 |
| `cart/cart.feature` | TC10-TC12, TC21 |
| `checkout/validation.feature` | TC13-TC16, TC20 |
| `checkout/order.feature` | TC17-TC19 |
| `security/access-control.feature` | TC25 |
| `compatibility/responsive.feature` | TC23, conditional profile |
| `compatibility/browser.feature` | TC26, browser-matrix profile |
| Manual records, no automation scenario | TC22, TC24, exploratory charter |

Multiple scenarios/examples may share the same official `@TCxx` tag when a workbook case contains several checks. Do not invent replacement official IDs without approval.
