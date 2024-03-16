package org.johnnyhe.dayleet.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TestCaseData {
    private String questionName;
    private List<QuestionTestCaseData> testCases;


    @Getter
    @Setter
    public static class QuestionTestCaseData {
        private String input;
        private String output;
    }
}
