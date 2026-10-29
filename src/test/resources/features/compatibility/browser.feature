@browser_matrix @compatibility
Feature: SauceDemo browser compatibility
  The core shopping flow should work in each configured supported browser.

  @P2
  @TC26
  Scenario: Complete the core shopping flow in the configured browser
    Given the active browser matches the configured browser
    And the user is on the SauceDemo login page
    When the user logs in as the standard user
    And the user adds "Sauce Labs Backpack" to the cart
    And the user opens the cart
    And the user starts checkout
    And the user submits valid checkout details
    And the user finishes the order
    Then the order confirmation is displayed
