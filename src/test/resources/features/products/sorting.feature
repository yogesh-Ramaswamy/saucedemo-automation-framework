@authenticated @products @sorting
Feature: SauceDemo product sorting
  Product sorting should order names and numeric prices according to the selected option.

  @P2
  @TC06
  Scenario: Sort products by name A to Z
    When the user sorts products by "Name (A to Z)"
    Then product names are ordered "ascending"

  @P2
  @TC07
  Scenario: Sort products by name Z to A
    When the user sorts products by "Name (Z to A)"
    Then product names are ordered "descending"

  @P2
  @TC08
  Scenario: Sort products by price low to high
    When the user sorts products by "Price (low to high)"
    Then product prices are ordered "ascending"

  @P2
  @TC09
  Scenario: Sort products by price high to low
    When the user sorts products by "Price (high to low)"
    Then product prices are ordered "descending"
