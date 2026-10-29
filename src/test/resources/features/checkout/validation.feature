@authenticated @checkout
Feature: SauceDemo checkout validation
  Checkout should validate required customer information and prevent invalid order states.

  @negative @P1
  @TC13
  Scenario: Checkout with first name missing
    Given "Sauce Labs Backpack" is in the cart
    When the user opens the cart
    And the user starts checkout
    And the user submits checkout details first name "", last name "Tester", postal code "10001"
    Then the checkout error is "Error: First Name is required"

  @negative @P1
  @TC14
  Scenario: Checkout with last name missing
    Given "Sauce Labs Backpack" is in the cart
    When the user opens the cart
    And the user starts checkout
    And the user submits checkout details first name "Ava", last name "", postal code "10001"
    Then the checkout error is "Error: Last Name is required"

  @negative @P1
  @TC15
  Scenario: Checkout with postal code missing
    Given "Sauce Labs Backpack" is in the cart
    When the user opens the cart
    And the user starts checkout
    And the user submits checkout details first name "Ava", last name "Tester", postal code ""
    Then the checkout error is "Error: Postal Code is required"

  @boundary @P2 @conditional
  @TC16
  Scenario: Whitespace-only checkout values are rejected
    Given "Sauce Labs Backpack" is in the cart
    When the user opens the cart
    And the user starts checkout
    And the user submits checkout details first name "   ", last name "   ", postal code "   "
    Then whitespace-only checkout details are rejected

  @boundary @P2 @conditional
  @TC20
  Scenario: An empty cart cannot be checked out
    Given the cart is empty
    When the user opens the cart
    And the user attempts to complete checkout with an empty cart
    Then the application prevents an empty order from being completed
