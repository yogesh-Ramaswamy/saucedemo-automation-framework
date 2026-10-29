@responsive @compatibility
Feature: SauceDemo responsive controls
  Core shopping controls should remain visible and usable at the workbook viewport widths.

  @P2
  @TC23
  Scenario Outline: Core shopping controls remain usable at different viewport widths
    Given the browser viewport is <width> by 900 pixels
    And the user is on the SauceDemo login page
    Then login controls are usable without horizontal overflow
    When the user logs in as the standard user
    Then product controls are usable without horizontal overflow
    When the user adds "Sauce Labs Backpack" to the cart
    And the user opens the cart
    Then cart controls are usable without horizontal overflow
    When the user starts checkout
    Then checkout controls are usable without horizontal overflow

    Examples:
      | width |
      | 1366  |
      | 768   |
      | 320   |
