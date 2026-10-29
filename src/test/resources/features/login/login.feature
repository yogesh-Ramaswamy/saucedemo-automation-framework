Feature: SauceDemo login
  Authentication should allow valid users and reject invalid or incomplete credentials.

  @smoke @P1
  @TC01
  Scenario: Standard user logs in successfully
    Given the user is on the SauceDemo login page
    When the user submits username "standard_user" and password "secret_sauce"
    Then the Products page is displayed

  @negative @P1
  @TC02
  Scenario: Login with an incorrect password
    Given the user is on the SauceDemo login page
    When the user submits username "standard_user" and password "incorrect password"
    Then the login error is "Epic sadface: Username and password do not match any user in this service"
    And the user remains on the login page

  @negative @P1
  @TC03
  Scenario: Locked-out user cannot log in
    Given the user is on the SauceDemo login page
    When the user submits username "locked_out_user" and password "secret_sauce"
    Then the login error is "Epic sadface: Sorry, this user has been locked out."
    And the user remains on the login page

  @negative @P2
  @TC04
  Scenario Outline: Login with empty required fields
    Given the user is on the SauceDemo login page
    When the user submits username "<username>" and password "<password>"
    Then the login error is "<error>"
    And the user remains on the login page

    Examples:
      | username      | password | error                               |
      |               |          | Epic sadface: Username is required  |
      | standard_user |          | Epic sadface: Password is required  |
