@authenticated @products
Feature: SauceDemo product listing
  Authenticated users should see complete product information and available actions.

  @P1
  @TC05
  Scenario: Products display complete visible information
    Then every product has a name, description, loaded image, price, and Add to Cart control
