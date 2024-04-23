package org.johnnyhe.dayleet.service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.Unirest;
import kong.unirest.HttpResponse;
import org.johnnyhe.dayleet.model.*;
import org.johnnyhe.dayleet.repository.TestCasePrintStatementRepo;
import org.johnnyhe.dayleet.repository.codingLangRepo;
import org.johnnyhe.dayleet.repository.questionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class Judge0Service2 {

    private final TestCasePrintStatementRepo myTestCasePrintStatementRepo;
    private final RestTemplate myRestTemplate;
    private final String judge0ApiUrl = "https://judge0-ce.p.rapidapi.com/submissions/";
    private final String judge0ApiKey = "a165254e0emshef4c605c96ac547p11debfjsn25f60946c55d";
    private final questionRepo myQuestionRepo;
    private final codingLangRepo myCodingLangRepo;

    private static final String COMMON_PYTHON_IMPORTS =
            "from math import sqrt, ceil, floor, pow, fabs\n" +
                    "from heapq import heappush, heappop, heapify\n" +
                    "from collections import defaultdict, Counter, deque\n" +
                    "from itertools import permutations, combinations, product\n" +
                    "from functools import lru_cache, reduce\n" +
                    "from bisect import bisect_left, bisect_right\n" +
                    "import sys\n" +
                    "import os\n" +
                    "from re import search, match, findall\n" +
                    "import string\n" +
                    "from random import randint, choice, shuffle\n";

    @Autowired
    public Judge0Service2(TestCasePrintStatementRepo myTestCasePrintStatementRepo, RestTemplate myRestTemplate, questionRepo myQuestionRepo, codingLangRepo myCodingLangRepo) {
        this.myTestCasePrintStatementRepo = myTestCasePrintStatementRepo;
        this.myRestTemplate = myRestTemplate;
        this.myQuestionRepo = myQuestionRepo;
        this.myCodingLangRepo = myCodingLangRepo;
    }

    public ExecutionResult execute(CodeSubmissionRequest submissionRequest) {
        int questionId = submissionRequest.getQuestionId();
        String userCode = submissionRequest.getCode();
        int langId = submissionRequest.getLanguageId();
        // mapping judge0's language to my database's language
        if (langId == 71) {
            langId = 1;
        }
        System.out.println("Here are the essential info:" + questionId + " " + langId);
//        List<TestCasePrintStatement> printStatements = myTestCasePrintStatementRepo.findByQuestionIdAndLanguageId(questionId, langId);

        question question = myQuestionRepo.findById((long) questionId).orElse(null);
        codingLang language = myCodingLangRepo.findById((long) langId).orElse(null);

        List<TestCasePrintStatement> printStatements = myTestCasePrintStatementRepo.findByQuestionAndLanguage(question, language);


        System.out.println("Here are my print statements:");
        System.out.println(printStatements);
        StringBuilder fullCode = new StringBuilder(COMMON_PYTHON_IMPORTS);
        fullCode.append(userCode);

        //add a new line before appending test cases
        if (!userCode.endsWith("\n")) {
            fullCode.append("\n");
        }



        printStatements.forEach(printStatement -> {
            fullCode.append(printStatement.getPrintStatement()).append("\n");
        });

        System.out.println("Here is the final code:");
        System.out.println(fullCode);
        // switching the languageId back for judge0 to interpret
        if (langId == 1) {
            langId = 71;
            System.out.println(langId);
        }
        return executeUserCode(fullCode.toString(), langId, question, language);
    }


    private ExecutionResult executeUserCode(String code, int langId, question question, codingLang language) {
        ExecutionResult result = new ExecutionResult();

        ObjectMapper mapper = new ObjectMapper();
        String requestBody;
        try {
            requestBody = mapper.writeValueAsString(Map.of(
                    "language_id", langId,
                    "source_code", code
            ));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            result.setErrorMsg("Error creating request body");
            return result;
        }

        System.out.println("Request Body: " + requestBody);

        HttpResponse<String> response = Unirest.post(judge0ApiUrl)
                .header("Content-Type", "application/json")
                .header("X-RapidAPI-Key", judge0ApiKey)
                .header("X-RapidAPI-Host", "judge0-ce.p.rapidapi.com")
                .body(requestBody)
                .asString();

        System.out.println("HTTP Status Code: " + response.getStatus());
        System.out.println("Status Text: " + response.getStatusText());
        System.out.println("Response Body: " + response.getBody());

        String token;


        try {
            JsonNode rootNode = mapper.readTree(response.getBody());
            token = rootNode.path("token").asText();
            System.out.println("Token: " + token);

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
                        result.setSuccessMsg(stdout);
                        System.out.println(stdout);


                        String[] actualOutputs = stdout.split("\n");
                        System.out.println("test case outputs" + Arrays.toString(actualOutputs));
                        System.out.println("Before going in for loop!");
                        List<TestCasePrintStatement> printStatements = myTestCasePrintStatementRepo.findByQuestionAndLanguage(question, language);
                        System.out.println("Print statements: " + printStatements);

                        for (int i = 0; i < actualOutputs.length; i++) {
                            String actualOutput = actualOutputs[i].trim();
//                            System.out.println("this test case output is: " + testCaseOutput);
                            long testCaseId;
                            if (question.getId() == 3) {
                                testCaseId = i + 13;
                            } else if (question.getId() == 2) {
                                testCaseId = i + 5;
                            } else if (question.getId() == 4) {
                                testCaseId = i + 18;
                            }
                            else {
                                testCaseId = (i + 1);
                            }
                            System.out.println("this test case id is: " + testCaseId);
                            System.out.println("this actual output is:" + actualOutput);
                            TestCaseResult displayResult = new TestCaseResult();
                            displayResult.setTestCaseId(testCaseId);
                            displayResult.setActualOutput(actualOutput);
                            System.out.println("Question is: " + question.getName());
                            System.out.println("Language is: " + language);

                            // Find the corresponding TestCasePrintStatement for the current test case ID
                            TestCasePrintStatement printStatement = printStatements.stream()
                                    .filter(ps -> ps.getId().equals(testCaseId))
                                    .findFirst()
                                    .orElse(null);
                            System.out.println("Corresponding print statement is: " + printStatement);


                            if (printStatement != null) {
                                String testCase = printStatement.getPrintStatement();
                                questionTestCase questionTestCase = printStatement.getQuestion().getQuestionTestCases().stream()
                                        .filter(tc -> tc.getId() == testCaseId)
                                        .findFirst()
                                        .orElse(null);
                                System.out.println("questionTestCase: " + questionTestCase);

                                if (questionTestCase != null) {
                                    System.out.println("Found questionTestCase for testCaseId: " + testCaseId);
                                    String expectedOutput = questionTestCase.getCorrectOutput();
                                    displayResult.setTestCase(testCase);
                                    displayResult.setExpectedOutput(expectedOutput);
                                    displayResult.setCorrect(actualOutput.equals(expectedOutput));
                                } else {
                                    System.out.println("No questionTestCase found for testCaseId: " + testCaseId);
                                }
                            }

                            result.addTestCaseResult(displayResult);
                        }
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(e);
                return result;
            } catch (JsonProcessingException e) {
                System.out.println(e);
                return result;
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            result.setErrorMsg("Error processing submission response");
            return result;
        }

//        result.setSuccessMsg("Code submitted successfully");
        return result;
    }
}
