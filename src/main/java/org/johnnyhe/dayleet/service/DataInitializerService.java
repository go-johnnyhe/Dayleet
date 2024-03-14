package org.johnnyhe.dayleet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.johnnyhe.dayleet.model.QuestionCollectionData;
import org.johnnyhe.dayleet.model.question;
import org.johnnyhe.dayleet.model.questionCollection;
import org.johnnyhe.dayleet.model.questionList;
import org.johnnyhe.dayleet.repository.questionCollectionRepo;
import org.johnnyhe.dayleet.repository.questionListRepo;
import org.johnnyhe.dayleet.repository.questionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class DataInitializerService {
    private final questionRepo myQuestionRepo;
    private final questionListRepo myQuestionListRepo;
    private final questionCollectionRepo myQuestionCollectionRepo;

    @Autowired
    public DataInitializerService(questionRepo myQuestionRepo, questionListRepo myQuestionListRepo, questionCollectionRepo myQuestionCollectionRepo) {
        this.myQuestionRepo = myQuestionRepo;
        this.myQuestionListRepo = myQuestionListRepo;
        this.myQuestionCollectionRepo = myQuestionCollectionRepo;
    }

    public void initializeData(String jsonData) throws IOException {
//        String data = Files.readString(Path.of("/Users/hemingao/Desktop/projects/questions.json"));
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<QuestionCollectionData> collectionsData = objectMapper.readValue(jsonData, new TypeReference<List<QuestionCollectionData>>() {
            });
            for (QuestionCollectionData collectionData : collectionsData) {
                questionCollection myQuestionCollection = new questionCollection();
                myQuestionCollection.setName(collectionData.getCollectionName());
                myQuestionCollectionRepo.save(myQuestionCollection);

                for (question q : collectionData.getQuestions()) {
                    if (!myQuestionRepo.existsByNameAndCategoryAndDifficultyAndDescription(q.getName(), q.getCategory(), q.getDifficulty(), q.getDescription())) {
                        myQuestionRepo.save(q);
                    } else {
                        q = myQuestionRepo.findByNameAndCategoryAndDifficultyAndDescription(q.getName(), q.getCategory(), q.getDifficulty(), q.getDescription());
                    }
                    questionList myQuestionList = new questionList();
                    myQuestionList.setQuestionCollection(myQuestionCollection);
                    myQuestionList.setQuestion(q);
                    myQuestionListRepo.save(myQuestionList);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println(e);
        }

    }

}
