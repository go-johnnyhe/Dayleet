package org.johnnyhe.dayleet.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

// ExecutionResult.java
@Getter
@Setter
public class ExecutionResult {
    private String errorMsg;
    private String successMsg;
    private List<TestCaseResult> testCaseResults = new ArrayList<>();

    public void addTestCaseResult(TestCaseResult testCaseResult) {
        testCaseResults.add(testCaseResult);
    }
}
