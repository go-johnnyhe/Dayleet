package org.johnnyhe.dayleet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.johnnyhe.dayleet.model.*;
import org.johnnyhe.dayleet.repository.*;
import org.johnnyhe.dayleet.service.Judge0Service;
import org.johnnyhe.dayleet.service.ProblemService;
import org.johnnyhe.dayleet.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@Controller
public class AuthController {
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final userRepo myUserRepo;
    private final questionListRepo myQuestionListRepo;
    private final questionRepo myQuestionRepo;
    private final questionPlaceholderRepo myQuestionPlaceHolderRepo;
    private final codingLangRepo myCodingLangRepo;
    private final ProblemService problemService;
    private final Judge0Service myJudge0Service;

    @Autowired
    public AuthController(UserDetailsServiceImpl userDetailsService, PasswordEncoder passwordEncoder, userRepo myUserRepo, questionListRepo myQuestionListRepo, questionRepo myQuestionRepo, questionPlaceholderRepo myQuestionPlaceHolderRepo, codingLangRepo myCodingLangRepo, ProblemService problemService, Judge0Service myJudge0Service) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.myUserRepo = myUserRepo;
        this.myQuestionListRepo = myQuestionListRepo;
        this.myQuestionRepo = myQuestionRepo;
        this.myQuestionPlaceHolderRepo = myQuestionPlaceHolderRepo;
        this.myCodingLangRepo = myCodingLangRepo;
        this.problemService = problemService;
        this.myJudge0Service = myJudge0Service;
    }

    @GetMapping("/")
    public String takeMeHome() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String showHome() {
        return "home";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new user());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") user myUser) {
        String encodedPassword = passwordEncoder.encode(myUser.getPassword());
        myUser.setPassword(encodedPassword);
        userDetailsService.registerUser(myUser);
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Principal principal, Model model) {
        String username = principal.getName();
        user myUser = myUserRepo.findByUsername(username);
        if (!myUser.isCompletedSetup()) {
            return "redirect:/setup";
        }
        model.addAttribute("username", username);

        String selectedQuestionSet = myUser.getQuestionSet();
        List<questionList> questionList = myQuestionListRepo.findByQuestionCollectionName(selectedQuestionSet);
        model.addAttribute("questionList", questionList);

        return "dashboard";
    }

    @GetMapping("/setup")
    public String showSetupPage(Model model) {
        model.addAttribute("questionSets", Arrays.asList("Blind75", "Neetcode150", "Grind169"));
        return "setup";
    }

    @PostMapping("/setup")
    public String processSetup(@RequestParam("questionSet") String questionSet,
                               @RequestParam("newQuestionsPerDay") int newQuestionsPerDay,
                               @RequestParam("completionDate") String completionDate,
                               Principal principal) {
        String username = principal.getName();
        user myUser = myUserRepo.findByUsername(username);

        myUser.setQuestionSet(questionSet);
        myUser.setNumDailyProblems(newQuestionsPerDay);
        myUser.setCompletionDate(LocalDate.parse(completionDate));
        myUser.setCompletedSetup(true);

        myUserRepo.save(myUser);
        return "redirect:/dashboard";
    }

//    @GetMapping("/problem")
//    public String showEditor() {
//        return "editor";
//    }

    @GetMapping("/problem/{id}")
    public String showProblem(@PathVariable("id") Long problemId, Model model) {
        question problem = myQuestionRepo.findById(problemId).orElse(null);

        if (problem != null) {
//            String languageId = "71";
            Optional<codingLang> optionalCodingLang = myCodingLangRepo.findByName("Python");
            codingLang language = optionalCodingLang.orElse(null);
            if (language != null) {
                questionPlaceHolder placeholder = myQuestionPlaceHolderRepo.findByQuestionAndCodingLanguage(problem, language);
                model.addAttribute("problem", problem);
                model.addAttribute("placeholder", placeholder != null ? placeholder.getPlaceHolder() : "");
            } else {
                model.addAttribute("problem", problem);
                model.addAttribute("placeholder", "");
            }
        }
        return "editor";
    }

    @PostMapping("/execute")
    public ResponseEntity<?> executeCode(@RequestBody CodeSubmissionRequest request) throws JsonProcessingException {
        String code = request.getCode();
        int languageId = request.getLanguageId();

        System.out.println("Received CodeSubmissionRequest: " + request);

        Long questionId = request.getQuestionId();
        if (questionId == null) {
            return ResponseEntity.badRequest().body("Question ID is required.");
        }

        question myQuestion = problemService.findQuestionById(request.getQuestionId());
        if (myQuestion == null) {
            return ResponseEntity.badRequest().body("Invalid question id");
        }

        List<String> testCases = problemService.getTestCases((long) myQuestion.getId());
        List<String> expectedOutputs = problemService.getExpectedOutputs((long) myQuestion.getId());

        List<Boolean> results = new ArrayList<>();

        for (int i = 0; i < testCases.size(); i++) {
            String testCase = testCases.get(i);
            String expectedOutput = expectedOutputs.get(i);

            String actualOutput = myJudge0Service.executeCode(code, languageId, testCase);

            boolean isCorrect;
            if (actualOutput == null) {
                isCorrect = false; // Consider null output as incorrect
            } else {
                isCorrect = actualOutput.trim().equals(expectedOutput.trim());
            }
            results.add(isCorrect);
        }

        boolean allPassed = results.stream().allMatch(result -> result);

//exp
        List<Map<String, Object>> testCaseResults = new ArrayList<>();

        for (int i = 0; i < testCases.size(); i++) {
            String testCase = testCases.get(i);
            String expectedOutput = expectedOutputs.get(i);

            String actualOutput = myJudge0Service.executeCode(code, languageId, testCase);

            boolean isCorrect = actualOutput != null && actualOutput.trim().equals(expectedOutput.trim());
            results.add(isCorrect);

            Map<String, Object> testCaseResult = new HashMap<>();
            testCaseResult.put("testCase", testCase);
            testCaseResult.put("expectedOutput", expectedOutput);
            testCaseResult.put("actualOutput", actualOutput);
            testCaseResult.put("isCorrect", isCorrect);
            testCaseResults.add(testCaseResult);
        }

//        exp end

        long passedTests = results.stream().filter(Boolean::booleanValue).count();

        // Prepare detailed response
        Map<String, Object> response = new HashMap<>();
        response.put("allPassed", allPassed);
        response.put("totalTests", testCases.size());
        response.put("passedTests", passedTests);

        List<String> detailMessages = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            if (!results.get(i)) {
                detailMessages.add(String.format("Test Case %d Failed", i + 1));
            }
        }

        if (!detailMessages.isEmpty()) {
            response.put("details", detailMessages);
        }

        response.put("testCaseResults", testCaseResults);
        return ResponseEntity.ok(response);
    }
}
