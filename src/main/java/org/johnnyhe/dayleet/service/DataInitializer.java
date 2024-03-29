package org.johnnyhe.dayleet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final DataInitializerService dataInitializerService;

    @Autowired
    public DataInitializer(DataInitializerService dataInitializerService) {
        this.dataInitializerService = dataInitializerService;
    }

    @Value("${data.init.enable}")
    private boolean initData;

    public void run(String... args) throws Exception{
        if (initData) {
            String jsonData = "[{\"collectionName\":\"Blind75\",\"questions\":[{\"name\":\"Two Sum\",\"difficulty\":\"Easy\",\"description\":\"Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.\\nYou may assume that each input would have exactly one solution, and you may not use the same element twice.\\nYou can return the answer in any order.\",\"category\":\"Arrays & Hashing\"},{\"name\":\"Valid Anagram\",\"difficulty\":\"Easy\",\"description\":\"An Anagram is a word or phrase formed by rearranging the letters of a different word or phrase, typically using all the original letters exactly once.\",\"category\":\"Arrays & Hashing\"},{\"name\":\"Contains Duplicate\",\"difficulty\":\"Easy\",\"description\":\"Given an integer array nums, return true if any value appears at least twice in the array, and return false if every element is distinct.\",\"category\":\"Arrays & Hashing\"}]},{\"collectionName\":\"Neetcode150\",\"questions\":[{\"name\":\"Two Sum\",\"difficulty\":\"Easy\",\"description\":\"Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.\\nYou may assume that each input would have exactly one solution, and you may not use the same element twice.\\nYou can return the answer in any order.\",\"category\":\"Arrays & Hashing\"},{\"name\":\"Valid Anagram\",\"difficulty\":\"Easy\",\"description\":\"An Anagram is a word or phrase formed by rearranging the letters of a different word or phrase, typically using all the original letters exactly once.\",\"category\":\"Arrays & Hashing\"},{\"name\":\"Contains Duplicate\",\"difficulty\":\"Easy\",\"description\":\"Given an integer array nums, return true if any value appears at least twice in the array, and return false if every element is distinct.\",\"category\":\"Arrays & Hashing\"}]},{\"collectionName\":\"Grind75\",\"questions\":[{\"name\":\"Two Sum\",\"difficulty\":\"Easy\",\"description\":\"Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.\\nYou may assume that each input would have exactly one solution, and you may not use the same element twice.\\nYou can return the answer in any order.\",\"category\":\"Arrays & Hashing\"},{\"name\":\"Valid Anagram\",\"difficulty\":\"Easy\",\"description\":\"An Anagram is a word or phrase formed by rearranging the letters of a different word or phrase, typically using all the original letters exactly once.\",\"category\":\"Arrays & Hashing\"},{\"name\":\"Contains Duplicate\",\"difficulty\":\"Easy\",\"description\":\"Given an integer array nums, return true if any value appears at least twice in the array, and return false if every element is distinct.\",\"category\":\"Arrays & Hashing\"}]},{\"collectionName\":\"Grind169\",\"questions\":[{\"name\":\"Two Sum\",\"difficulty\":\"Easy\",\"description\":\"Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.\\nYou may assume that each input would have exactly one solution, and you may not use the same element twice.\\nYou can return the answer in any order.\",\"category\":\"Arrays & Hashing\"},{\"name\":\"Valid Anagram\",\"difficulty\":\"Easy\",\"description\":\"An Anagram is a word or phrase formed by rearranging the letters of a different word or phrase, typically using all the original letters exactly once.\",\"category\":\"Arrays & Hashing\"},{\"name\":\"Contains Duplicate\",\"difficulty\":\"Easy\",\"description\":\"Given an integer array nums, return true if any value appears at least twice in the array, and return false if every element is distinct.\",\"category\":\"Arrays & Hashing\"}]}]";
            dataInitializerService.initializeData(jsonData);
        }
    }

}
