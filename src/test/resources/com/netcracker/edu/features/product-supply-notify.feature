@Notify
Feature: Is product supply notify
  Notification about supply should be send

  Background:
    Given Remove all products
    Given Enable wire-mock server
  Scenario: Product supply notify
    #There are no products in the inventory
    Given There are available 0 products
    When Send supply order that contains product with name "Paper A4" and description "White 70g"
    Then Check supply notifications product with name "Paper A4"
    Then Check sold product with name "Paper A4"
