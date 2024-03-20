package org.johnnyhe.dayleet.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeSubmissionRequest {
    private Long questionId;
    private String code;
    private Integer languageId;

    @Override
    public String toString() {
        return "CodeSubmissionRequest{" +
                "questionId=" + questionId +
                ", code='" + code + '\'' +
                ", languageId=" + languageId +
                '}';
    }
}
