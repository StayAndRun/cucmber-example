@Supply
Feature: Is product supplied
  The product should available at Inventory after supply order on Order-manager

  Background:
    Given Remove all products
  Scenario: Product supply
    #There are no products in the inventory
    Given There are available 0 products
    When Send supply order that contains product with name "Paper A4" and description "White 70g"
    Then There are available 1 products
    And Available product with name "Paper A4" and description "White 70g"