@authenticated @cart
Feature: SauceDemo cart management
  Cart contents and badge state should remain consistent after product actions.

  @P1
  @TC10
  Scenario: Add two products and verify the cart badge and contents
    When the user adds "Sauce Labs Backpack" to the cart
    Then the cart badge count is 1
    When the user adds "Sauce Labs Bike Light" to the cart
    Then the cart badge count is 2
    When the user opens the cart
    Then the cart contains exactly:
      | Sauce Labs Backpack |
      | Sauce Labs Bike Light |

  @P2
  @TC11
  Scenario: Remove a product from the Products page
    Given "Sauce Labs Backpack" is in the cart
    Then the cart badge count is 1
    When the user removes "Sauce Labs Backpack" from the Products page
    Then the cart badge count is 0
    When the user opens the cart
    Then the cart does not contain "Sauce Labs Backpack"

  @P2
  @TC12
  Scenario: Remove a product from the Cart page
    Given the following products are in the cart:
      | Sauce Labs Backpack |
      | Sauce Labs Bike Light |
    When the user opens the cart
    And the user removes "Sauce Labs Backpack" from the Cart page
    Then the cart badge count is 1
    And the cart contains "Sauce Labs Bike Light"
    And the cart does not contain "Sauce Labs Backpack"

  @P2 @conditional
  @TC21
  Scenario: The same product cannot be added twice through its visible control
    Given "Sauce Labs Backpack" is in the cart
    Then the Products page does not offer another Add to Cart action for "Sauce Labs Backpack"
    And the cart badge count is 1
    When the user opens the cart
    Then the cart contains 1 item
    And the cart contains "Sauce Labs Backpack"
