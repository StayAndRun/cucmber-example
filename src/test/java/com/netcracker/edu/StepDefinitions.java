package com.netcracker.edu;

import com.netcracker.edu.model.Product;
import com.netcracker.edu.model.SupplyOrder;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.messages.internal.com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.messages.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.messages.internal.com.fasterxml.jackson.databind.type.CollectionType;
import okhttp3.*;
import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;
import org.opentest4j.TestAbortedException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class StepDefinitions {

    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @ParameterType("INVENTORY|ORDER_MANAGER")
    public ServicesEnum serviceEnum(String service) {
        return ServicesEnum.valueOf(service);
    }

    @Given("Request GET {string} from {serviceEnum} return {string} entity;")
    public void requestGETFromReturnEmpty(String path, ServicesEnum service) {

    }

    @When("Send supply order that contains product with name {string} and description {string}")
    public void sendSupplyOrderThatContainsProductWithNameAndDescription(String name, String description) {
        Request request = new Request.Builder()
                .post(RequestBody.create(MediaType.parse("application/json"), parse(
                        new SupplyOrder(
                                Collections.singletonList(new Product(name, description)), "supply"))
                        )
                )
                .url(ServicesEnum.ORDER_MANAGER.getUrl() + "/api/v1/orders/supply")
                .build();
        Response response = executeRequest(request);
        Assertions.assertEquals(200, response.code(), "Unexpected response code: " + response.message());
    }

    @Given("There are available {int} products")
    public void thereAreAvailableProducts(int expectedNumber) {
        Request request = new Request.Builder()
                .get()
                .url(ServicesEnum.INVENTORY.getUrl() + "/api/v1/products")
                .build();
        List<Product> response = parseListResponse(executeRequest(request), Product.class);
        int actualSize = response.size();
        Assertions.assertEquals(expectedNumber, actualSize);
    }

    @Then("Available product with name {string} and description {string}")
    public void availableProductWithNameAndDescription(String name, String description) {
        Request request = new Request.Builder()
                .get()
                .url(ServicesEnum.INVENTORY.getUrl() + "/api/v1/products")
                .build();
        List<Product> response = parseListResponse(executeRequest(request), Product.class);
        Product product = response.get(0);
        Assertions.assertEquals(name, product.getName());
        Assertions.assertEquals(description, product.getDescription());
    }

    @When("Send POST {string} to {serviceEnum} with body {string}")
    public void sendPOSTToORDER_MANAGERWithBody(String path, ServicesEnum service, String body) {

    }

    private <BODY_TYPE> List<BODY_TYPE> parseListResponse(Response response, Class<BODY_TYPE> typeClass) {
        try {
            String body = Objects.requireNonNull(response.body()).string();
            CollectionType type = objectMapper.getTypeFactory().constructCollectionType(List.class, typeClass);
            return objectMapper.readValue(body, type);
        } catch (IOException e) {
            throw new AssertionFailedError("Failed execute request: ", e);
        }
    }

    private <TYPE> String parse(TYPE content) {
        try {
            return objectMapper.writeValueAsString(content);
        } catch (JsonProcessingException e) {
            throw new TestAbortedException("Failed parsing: ", e);
        }
    }

    private Response executeRequest(Request request) {
        try {
            return httpClient.newCall(request).execute();
        } catch (IOException e) {
            throw new TestAbortedException("Failed execute request: ", e);
        }
    }

    @Given("Remove all products")
    public void removeAllProducts() {
        Request request = new Request.Builder()
                .delete()
                .url(ServicesEnum.INVENTORY.getUrl() + "/api/v1/products")
                .build();
        Response response = executeRequest(request);
        Assertions.assertEquals(204, response.code());
    }

    @Then("Check sold product with name {string}")
    public void checkSoldProductWithName(String name) {

    }
}
