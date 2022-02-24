Feature: Is product supplied
  The product should available at Inventory after supply order on Order-manager

  Scenario: Product supply
    #There are no products in the inventory
    Given Request GET "/api/v1/products" from INVENTORY return "0" entity;
    When Send POST "/api/v1/orders/supply" to ORDER_MANAGER with body "product"
    Then Request GET "/api/v1/products" from INVENTORY return "1" entity;