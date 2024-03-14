package org.johnnyhe.dayleet.model;

import java.util.List;


public class QuestionCollectionData {
    private String collectionName;
    private List<question> questions;

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public List<question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<question> questions) {
        this.questions = questions;
    }
}
