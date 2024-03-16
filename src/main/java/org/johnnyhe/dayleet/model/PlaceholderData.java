package org.johnnyhe.dayleet.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlaceholderData {
    private String languageName;
    private List<QuestionPlaceholderData> questions;

    @Setter
    @Getter
    public static class QuestionPlaceholderData {
        private String name;
        private String placeholder;

    }
}
