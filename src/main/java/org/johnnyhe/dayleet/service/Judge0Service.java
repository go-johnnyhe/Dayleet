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
        // splitting example
        String processedCode = getProcessedCode(code, testCase);

        // Encoding
        String encodedCode = Base64.getEncoder().encodeToString(processedCode.getBytes());
        String encodedTestCase = Base64.getEncoder().encodeToString(testCase.getBytes());

        // Submission
        HttpResponse<String> response = Unirest.post(judge0ApiUrl + "?base64_encoded=true&fields=*")
                .header("content-type", "application/json")
                .header("X-RapidAPI-Key", judge0ApiKey)
                .header("X-RapidAPI-Host", "judge0-ce.p.rapidapi.com")
                .body(String.format("{\n" +
                        "    \"language_id\": %d,\n" +
                        "    \"source_code\": \"%s\",\n" +
                        "    \"stdin\": \"%s\"\n" +
                        "}", languageId, encodedCode, encodedTestCase))
                .asString();

        // Token extraction
        ObjectMapper mapper = new ObjectMapper();
        String token;
        try {
            JsonNode rootNode = mapper.readTree(response.getBody());
            token = rootNode.path("token").asText();
            System.out.println("Here's token" + token);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error processing submission response";
        }

        // Polling for result
        String stdout = null;
        try {
            boolean isProcessing = true;
            while (isProcessing) {
                Thread.sleep(2000); // Delay before checking status again
                HttpResponse<String> getResponse = Unirest.get(judge0ApiUrl + token + "?base64_encoded=false&fields=*")
                        .header("X-RapidAPI-Key", judge0ApiKey)
                        .header("X-RapidAPI-Host", "judge0-ce.p.rapidapi.com")
                        .asString();

                JsonNode jsonResponse = mapper.readTree(getResponse.getBody());
                int statusId = jsonResponse.path("status").path("id").asInt();
                isProcessing = statusId == 1 || statusId == 2; // 1: In Queue, 2: Processing

                if (!isProcessing) {
                    if (jsonResponse.has("stderr")) {
                        System.out.println("Stderr 1: " + jsonResponse.path("stderr").asText());
                    }
                    if (jsonResponse.has("message")) {
                        System.out.println("Message: " + jsonResponse.path("message").asText());
                    }
                    stdout = jsonResponse.path("stdout").asText();
                    System.out.println(stdout);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Execution was interrupted";
        } catch (JsonProcessingException e) {
            return "Error processing execution result";
        }

        return stdout != null ? stdout : "No output or execution error";
    }

    private static String getProcessedCode(String code, String testCase) {
        String[] parts = testCase.split("], ");
        String arrayPart = parts[0] + "]"; // Adds the closing bracket back
        String integerPart = parts[1];

        System.out.println("the following two prints are test cases");
        System.out.println(arrayPart);
        System.out.println(integerPart);
        // Ensure to properly extract and format the test case data
        // This might involve more sophisticated parsing depending on the exact format and complexity of your test cases

        // Prepend the necessary import statement to the user's code
        String finalCode = "from typing import List\n" + code;

        // Insert the dynamically determined test case values into the processed code
        return finalCode + "\nif __name__ == \"__main__\":\n" +
                "    nums = " + arrayPart + "\n" +
                "    target = " + integerPart + "\n" +
                "    print(twoSum(nums, target))";
    }
}
