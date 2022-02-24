package com.netcracker.edu;

public enum ServicesEnum {
    INVENTORY ("INVENTORY_URL"),
    ORDER_MANAGER ("ORDER_MANAGER_URL");

    private final String serviceUrl;

    ServicesEnum(String serviceUrl) {

        this.serviceUrl = serviceUrl;
    }

    public String getUrl() {
        return System.getenv(serviceUrl);
    }
}
