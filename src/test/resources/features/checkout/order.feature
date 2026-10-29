@authenticated @checkout
Feature: SauceDemo order checkout
  Valid orders should show consistent totals and support finish or cancel behavior.

  @P1 @conditional
  @TC17
  Scenario: Verify subtotal, tax, and final total arithmetic
    Given the following products are in the cart:
      | Sauce Labs Backpack |
      | Sauce Labs Bike Light |
    When the user opens the cart
    And the user starts checkout
    And the user submits valid checkout details
    Then checkout overview contains exactly:
      | Sauce Labs Backpack |
      | Sauce Labs Bike Light |
    And the overview subtotal, tax, and total are arithmetically consistent

  @smoke @P1 @conditional
  @TC18
  Scenario: Finish the order and verify confirmation and empty cart
    Given "Sauce Labs Backpack" is in the cart
    When the user opens the cart
    And the user starts checkout
    And the user submits valid checkout details
    And the user finishes the order
    Then the order confirmation is displayed
    And the cart is empty after the order

  @P1 @conditional
  @TC19
  Scenario: Cancel checkout without placing an order
    Given "Sauce Labs Backpack" is in the cart
    When the user opens the cart
    And the user starts checkout
    And the user submits valid checkout details
    And the user cancels from Checkout Overview
    Then the Products page is displayed
    And no order confirmation is displayed
    And the cart badge count is 1
    When the user opens the cart
    Then the cart contains "Sauce Labs Backpack"
