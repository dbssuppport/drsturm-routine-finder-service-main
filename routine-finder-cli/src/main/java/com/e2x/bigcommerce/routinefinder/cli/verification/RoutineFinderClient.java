package com.e2x.bigcommerce.routinefinder.cli.verification;

import com.e2x.bigcommerce.routinefindermodel.RoutineEnquiry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
public class RoutineFinderClient {

    private final String routineFinderServiceUrl;
    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public RoutineFinderClient(String routineFinderServiceUrl) {
        this.routineFinderServiceUrl = routineFinderServiceUrl;
    }

    public RoutineEnquiry startRoutineEnquiry() {
        var httpRequest = HttpRequest
                .newBuilder(URI.create(getServiceUrl()))
                .setHeader("accept", "application/json")
                .GET()
                .build();

        return executeHttpRequest(httpRequest);
    }

    public RoutineEnquiry submitRoutineEnquiry(RoutineEnquiry routineEnquiry) {
        try {
            HttpRequest httpRequest = HttpRequest
                    .newBuilder(URI.create(getServiceUrl()))
                    .setHeader("accept", "application/json")
                    .setHeader("content-type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(routineEnquiry)))
                    .build();

            return executeHttpRequest(httpRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private RoutineEnquiry executeHttpRequest(HttpRequest httpRequest) {
        try {
            var body = httpClient
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString())
                    .body();

            return objectMapper.readValue(body, RoutineEnquiry.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String getServiceUrl() {
        return String.format("%s/bc/store/1/customer/1/routine", routineFinderServiceUrl);
    }
}
