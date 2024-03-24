package org.johnnyhe.dayleet.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestCaseResult {
    private Long testCaseId;
    private String testCase;
    private String actualOutput;
    private String expectedOutput;
    private boolean isCorrect;
}
