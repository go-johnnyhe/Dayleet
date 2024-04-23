package org.johnnyhe.dayleet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.johnnyhe.dayleet.model.*;
import org.johnnyhe.dayleet.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class DataInitializerService {
    private final questionRepo myQuestionRepo;
    private final questionListRepo myQuestionListRepo;
    private final questionCollectionRepo myQuestionCollectionRepo;
    private final codingLangRepo myCodingLangRepo;
    private final questionPlaceholderRepo myQuestionPlaceholderRepo;
    private final questionTestCaseRepo myQuestionTestCaseRepo;

    @Autowired
    public DataInitializerService(questionRepo myQuestionRepo, questionListRepo myQuestionListRepo, questionCollectionRepo myQuestionCollectionRepo, codingLangRepo myCodingLangRepo, questionPlaceholderRepo myQuestionPlaceholderRepo, questionTestCaseRepo myQuestionTestCaseRepo) {
        this.myQuestionRepo = myQuestionRepo;
        this.myQuestionListRepo = myQuestionListRepo;
        this.myQuestionCollectionRepo = myQuestionCollectionRepo;
        this.myCodingLangRepo = myCodingLangRepo;
        this.myQuestionPlaceholderRepo = myQuestionPlaceholderRepo;
        this.myQuestionTestCaseRepo = myQuestionTestCaseRepo;
    }

    public void initializeData(String jsonData) throws IOException {
//        String data = Files.readString(Path.of("/Users/hemingao/Desktop/projects/questions.json"));
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            List<QuestionCollectionData> collectionsData = objectMapper.readValue(jsonData, new TypeReference<List<QuestionCollectionData>>() {
//            });
//            for (QuestionCollectionData collectionData : collectionsData) {
//                questionCollection myQuestionCollection = new questionCollection();
//                myQuestionCollection.setName(collectionData.getCollectionName());
//                myQuestionCollectionRepo.save(myQuestionCollection);
//
//                for (question q : collectionData.getQuestions()) {
//                    if (!myQuestionRepo.existsByNameAndCategoryAndDifficultyAndDescription(q.getName(), q.getCategory(), q.getDifficulty(), q.getDescription())) {
//                        myQuestionRepo.save(q);
//                    } else {
//                        q = myQuestionRepo.findByNameAndCategoryAndDifficultyAndDescription(q.getName(), q.getCategory(), q.getDifficulty(), q.getDescription());
//                    }
//                    questionList myQuestionList = new questionList();
//                    myQuestionList.setQuestionCollection(myQuestionCollection);
//                    myQuestionList.setQuestion(q);
//                    myQuestionListRepo.save(myQuestionList);
//                }
//            }
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            System.out.println(e);
//        }
//
//        String jsonPlaceHolderData = "[\n" +
//                "  {\n" +
//                "    \"languageName\": \"Python\",\n" +
//                "    \"questions\": [\n" +
//                "      {\n" +
//                "        \"name\": \"Two Sum\",\n" +
//                "        \"placeholder\": \"def twoSum(self, nums: List[int], target: int) -> List[int]:\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"name\": \"Valid Anagram\",\n" +
//                "        \"placeholder\": \"def isAnagram(self, s: str, t: str) -> bool:\"\n" +
//                "      }\n" +
//                "    ]\n" +
//                "  },\n" +
//                "  {\n" +
//                "    \"languageName\": \"JavaScript\",\n" +
//                "    \"questions\": [\n" +
//                "      {\n" +
//                "        \"name\": \"Two Sum\",\n" +
//                "        \"placeholder\": \"function twoSum(nums, target) {\\n  // Your code here\\n}\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"name\": \"Valid Anagram\",\n" +
//                "        \"placeholder\": \"function isAnagram(s, t) {\\n  // Your code here\\n}\"\n" +
//                "      }\n" +
//                "    ]\n" +
//                "  },\n" +
//                "  {\n" +
//                "    \"languageName\": \"Java\",\n" +
//                "    \"questions\": [\n" +
//                "      {\n" +
//                "        \"name\": \"Two Sum\",\n" +
//                "        \"placeholder\": \"public int[] twoSum(int[] nums, int target) {\\n  // Your code here\\n}\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"name\": \"Valid Anagram\",\n" +
//                "        \"placeholder\": \"public boolean isAnagram(String s, String t) {\\n  // Your code here\\n}\"\n" +
//                "      }\n" +
//                "    ]\n" +
//                "  }\n" +
//                "]\n";
//        try {
//            List<PlaceholderData> placeHolderList = objectMapper.readValue(jsonPlaceHolderData, new TypeReference<List<PlaceholderData>>(){});
//            for (PlaceholderData placeholderData : placeHolderList) {
//                String languageName = placeholderData.getLanguageName();
//                Optional<codingLang> optionalCodingLang = myCodingLangRepo.findByName(languageName);
//                codingLang codingLang;
//                if (optionalCodingLang.isPresent()) {
//                    codingLang = optionalCodingLang.get();
//                } else {
//                    codingLang = new codingLang();
//                    codingLang.setName(languageName);
//                }
//                myCodingLangRepo.save(codingLang);
//
//                for (PlaceholderData.QuestionPlaceholderData questionPlaceholderData : placeholderData.getQuestions()) {
//                    String questionName = questionPlaceholderData.getName();
//                    question myQuestion = myQuestionRepo.findByName(questionName);
//                    if (myQuestion == null) {
//                        continue;
//                    }
//                    questionPlaceHolder myQuestionPlaceHolder = new questionPlaceHolder();
//                    myQuestionPlaceHolder.setPlaceHolder(questionPlaceholderData.getPlaceholder());
//                    myQuestionPlaceHolder.setCodingLanguage(codingLang);
//                    myQuestionPlaceHolder.setQuestion(myQuestion);
//                    myQuestionPlaceholderRepo.save(myQuestionPlaceHolder);
//                }
//            }
//
//        } catch (JsonProcessingException e) {
//            System.out.println("Problems parsing placeholder JSON");
//        }
//
//        String jsonTestCaseData = "[\n" +
//                "  {\n" +
//                "    \"questionName\": \"Two Sum\",\n" +
//                "    \"testCases\": [\n" +
//                "      {\n" +
//                "        \"input\": \"[2, 7, 11, 15], 9\",\n" +
//                "        \"output\": \"[0, 1]\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"input\": \"[3, 2, 4], 6\",\n" +
//                "        \"output\": \"[1, 2]\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"input\": \"[3, 3], 6\",\n" +
//                "        \"output\": \"[0, 1]\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"input\": \"[1, 2, 3, 4, 5], 10\",\n" +
//                "        \"output\": \"[]\"\n" +
//                "      }\n" +
//                "    ]\n" +
//                "  },\n" +
//                "  {\n" +
//                "    \"questionName\": \"Contains Duplicate\",\n" +
//                "    \"testCases\": [\n" +
//                "      {\n" +
//                "        \"input\": \"[1, 2, 3, 1]\",\n" +
//                "        \"output\": \"true\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"input\": \"[1, 2, 3, 4]\",\n" +
//                "        \"output\": \"false\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"input\": \"[1, 1, 1, 3, 3, 4, 3, 2, 4, 2]\",\n" +
//                "        \"output\": \"true\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"input\": \"[]\",\n" +
//                "        \"output\": \"false\"\n" +
//                "      }\n" +
//                "    ]\n" +
//                "  },\n" +
//                "  {\n" +
//                "    \"questionName\": \"Valid Anagram\",\n" +
//                "    \"testCases\": [\n" +
//                "      {\n" +
//                "        \"input\": \"\\\"anagram\\\", \\\"nagaram\\\"\",\n" +
//                "        \"output\": \"true\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"input\": \"\\\"rat\\\", \\\"car\\\"\",\n" +
//                "        \"output\": \"false\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"input\": \"\\\"listen\\\", \\\"silent\\\"\",\n" +
//                "        \"output\": \"true\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"input\": \"\\\"hello\\\", \\\"world\\\"\",\n" +
//                "        \"output\": \"false\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"input\": \"\\\"\\\", \\\"\\\"\",\n" +
//                "        \"output\": \"true\"\n" +
//                "      }\n" +
//                "    ]\n" +
//                "  }\n" +
//                "]";
//
//        try {
//            List<TestCaseData> testCaseDataList = objectMapper.readValue(jsonTestCaseData, new TypeReference<List<TestCaseData>>() {
//            });
//            for (TestCaseData testCaseData : testCaseDataList) {
//                question myQuestion = myQuestionRepo.findByName(testCaseData.getQuestionName());
//
//                for (TestCaseData.QuestionTestCaseData testCase : testCaseData.getTestCases()) {
//                    questionTestCase myQuestionTestCase = new questionTestCase();
//                    myQuestionTestCase.setQuestion(myQuestion);
//                    myQuestionTestCase.setTestCase(testCase.getInput());
//                    myQuestionTestCase.setCorrectOutput(testCase.getOutput());
//                    myQuestionTestCaseRepo.save(myQuestionTestCase);
//                }
//            }
//
//        } catch (JsonProcessingException e) {
//            System.out.println("problems parsing test case JSON");
//            e.printStackTrace();
//        }

    }

}
