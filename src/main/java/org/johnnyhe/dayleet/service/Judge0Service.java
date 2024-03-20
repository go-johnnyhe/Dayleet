package org.johnnyhe.dayleet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class Judge0Service {
    private final RestTemplate myRestTemplate;
    private final String judge0ApiUrl = "https://judge0-ce.p.rapidapi.com/submissions/";
    private final String judge0ApiKey = "a165254e0emshef4c605c96ac547p11debfjsn25f60946c55d";
    public Judge0Service(RestTemplate myRestTemplate) {
        this.myRestTemplate = myRestTemplate;
    }

    public String executeCode(String code, Integer languageId, String testCase) {
        // Encode the input code to base64
        String encodedCode = Base64.getEncoder().encodeToString(code.getBytes());

        // Optionally, encode the test case to base64 if your application requires it
        String encodedTestCase = Base64.getEncoder().encodeToString(testCase.getBytes());

        HttpResponse<String> response = Unirest.post("https://judge0-ce.p.rapidapi.com/submissions?base64_encoded=true&fields=*")
                .header("content-type", "application/json")
                .header("X-RapidAPI-Key", "a165254e0emshef4c605c96ac547p11debfjsn25f60946c55d")
                .header("X-RapidAPI-Host", "judge0-ce.p.rapidapi.com")
                .body(String.format("{\n" +
                        "    \"language_id\": %d,\n" + // Dynamic language ID
                        "    \"source_code\": \"%s\",\n" + // Dynamic source code, now base64 encoded
                        "    \"stdin\": \"%s\"\n" + // Dynamic stdin, base64 encoded if necessary
                        "}", languageId, encodedCode, encodedTestCase))
                .asString();

        String responseBody = response.getBody();
        System.out.println("Here's your response body:");
        System.out.println(responseBody);
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(responseBody);
            String token = rootNode.path("token").asText();
            System.out.println(token);
            HttpResponse<String> getResponse = Unirest.get("https://judge0-ce.p.rapidapi.com/submissions/" + token + "?base64_encoded=false&fields=*")
                    .header("X-RapidAPI-Key", "a165254e0emshef4c605c96ac547p11debfjsn25f60946c55d")
                    .header("X-RapidAPI-Host", "judge0-ce.p.rapidapi.com")
                    .asString();
            String getResponseBody = getResponse.getBody();
            System.out.println(getResponseBody);

            JsonObject jsonResponse = JsonParser.parseString(getResponseBody).getAsJsonObject();
            int statusId = jsonResponse.get("status").getAsJsonObject().get("id").getAsInt();

// Polling loop
            while (statusId == 1 || statusId == 2) { // 1 for "In Queue", 2 for "Processing"
                // Wait before checking again
                try {
                    Thread.sleep(2000); // Wait for 2 seconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }

                // Check status again
                getResponse = Unirest.get("https://judge0-ce.p.rapidapi.com/submissions/" + token + "?base64_encoded=false&fields=*")
                        .header("X-RapidAPI-Key", "a165254e0emshef4c605c96ac547p11debfjsn25f60946c55d")
                        .header("X-RapidAPI-Host", "judge0-ce.p.rapidapi.com")
                        .asString();
                getResponseBody = getResponse.getBody();
                System.out.println(getResponseBody);

                jsonResponse = JsonParser.parseString(getResponseBody).getAsJsonObject();
                statusId = jsonResponse.get("status").getAsJsonObject().get("id").getAsInt();
            }

// Final status
            System.out.println("Final status: " + jsonResponse.get("status").getAsJsonObject().get("description").getAsString());

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return "";
    }

}
