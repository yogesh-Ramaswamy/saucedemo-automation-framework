@security @negative
Feature: SauceDemo logged-out access control
  Protected shopping pages should not be accessible after logout.

  @P1
  @TC25
  Scenario Outline: Open a protected inner-page URL after logging out
    Given the user is on the SauceDemo login page
    When the user logs in as the standard user
    And the user logs out
    And the user opens protected path "<path>"
    Then protected content is not accessible
    And the login page is displayed

    Examples:
      | path                    |
      | /inventory.html         |
      | /cart.html              |
      | /checkout-step-one.html |
