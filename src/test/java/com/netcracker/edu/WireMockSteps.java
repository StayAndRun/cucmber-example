package com.netcracker.edu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.netcracker.edu.model.SupplyOrder;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;
import org.opentest4j.TestAbortedException;

import java.io.IOException;
import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class WireMockSteps {


    private WireMockServer wireMockServer;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Given("Enable wire-mock server")
    public void enableWireMockServer() {
        wireMockServer = new WireMockServer(options().port(8089));
        StubMapping mapping = WireMock.post(
                WireMock.urlPathEqualTo("/notify"))
                .willReturn(WireMock.ok())
                .build();
        wireMockServer.addStubMapping(mapping);
        wireMockServer.start();
    }

    @Then("Check supply notifications product with name {string}")
    public void checkSupplyNotificationsProductWithName(String name) {
        List<LoggedRequest> requests = wireMockServer.findAll(RequestPatternBuilder.allRequests());
        Assertions.assertEquals(requests.size(), 1);
        String bodyAsString = requests.get(0).getBodyAsString();
        try {
            SupplyOrder supplyOrder = objectMapper.readValue(bodyAsString, SupplyOrder.class);
            Assertions.assertEquals(supplyOrder.getProducts().get(0).getName(), name);
        } catch (JsonProcessingException e) {
            throw new TestAbortedException("Supply parsing failed", e);
        }
        wireMockServer.stop();
    }

    private <BODY_TYPE> List<BODY_TYPE> parseListResponse(String body, Class<BODY_TYPE> typeClass) {
        try {

            CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, typeClass);
            return objectMapper.readValue(body, collectionType);
        } catch (IOException e) {
            throw new AssertionFailedError("Failed execute request: ", e);
        }
    }

}
