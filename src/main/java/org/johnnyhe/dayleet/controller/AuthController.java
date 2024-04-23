package org.johnnyhe.dayleet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.johnnyhe.dayleet.model.*;
import org.johnnyhe.dayleet.repository.*;
import org.johnnyhe.dayleet.service.Judge0Service;
import org.johnnyhe.dayleet.service.Judge0Service2;
import org.johnnyhe.dayleet.service.ProblemService;
import org.johnnyhe.dayleet.service.UserDetailsServiceImpl;
import org.johnnyhe.dayleet.service.UserProgressService;
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
    private final Judge0Service2 myJudge0Service2;

    private final UserProgressService myUserProgressService;

    @Autowired
    public AuthController(UserDetailsServiceImpl userDetailsService, PasswordEncoder passwordEncoder, userRepo myUserRepo, questionListRepo myQuestionListRepo, questionRepo myQuestionRepo, questionPlaceholderRepo myQuestionPlaceHolderRepo, codingLangRepo myCodingLangRepo, ProblemService problemService, Judge0Service myJudge0Service, Judge0Service2 myJudge0Service2, UserProgressService myUserProgressService) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.myUserRepo = myUserRepo;
        this.myQuestionListRepo = myQuestionListRepo;
        this.myQuestionRepo = myQuestionRepo;
        this.myQuestionPlaceHolderRepo = myQuestionPlaceHolderRepo;
        this.myCodingLangRepo = myCodingLangRepo;
        this.problemService = problemService;
        this.myJudge0Service = myJudge0Service;
        this.myJudge0Service2 = myJudge0Service2;
        this.myUserProgressService = myUserProgressService;
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
        System.out.println("registration you smart ass!");
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
        Optional<user> myUserOpt = myUserRepo.findByUsername(username);

        // Handle the case where the user is not found
        if (!myUserOpt.isPresent()) {
            // Potentially redirect to a login page or error page
            return "redirect:/login";
        }

        user myUser = myUserOpt.get();
        if (!myUser.isCompletedSetup()) {
            return "redirect:/setup";
        }
        model.addAttribute("username", username);

        String selectedQuestionSet = myUser.getQuestionSet();
        List<questionList> questionList = myQuestionListRepo.findByQuestionCollection_Name(selectedQuestionSet);
        model.addAttribute("questionList", questionList);

        int userId = myUser.getId();
        List<question> reviewQuestions = myUserProgressService.getReviewQuestions(userId);
        model.addAttribute("reviewQuestions", reviewQuestions);

        List<question> newQuestions = myUserProgressService.getNewQuestions(userId);
        model.addAttribute("newQuestions", newQuestions);

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
        Optional<user> myUserOpt = myUserRepo.findByUsername(username);

        // Check if the user was found
        if (!myUserOpt.isPresent()) {
            // Redirect to a login or error page since the user must be logged in to set up
            return "redirect:/login";
        }

        user myUser = myUserOpt.get();

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
    public String executeCode(@RequestParam("code") String code,
                               @RequestParam("languageId") int languageId,
                               @RequestParam("problemId") int problemId,
                               Model model) {

        System.out.println("I'm being called again!");
        CodeSubmissionRequest request = new CodeSubmissionRequest();
        request.setCode(code);
        request.setLanguageId(languageId);
        request.setQuestionId(problemId);
        ExecutionResult result = myJudge0Service2.execute(request);

        model.addAttribute("result", result);

        return "resultSnippet";
    }

    @PostMapping("/submit-rating")
    public ResponseEntity<?> submitRating(@RequestBody RatingDTO ratingDto, Principal principal) {
        String username = principal.getName();
        Optional<user> myUser = myUserRepo.findByUsername(username);
        if (myUser.isPresent()) {
            myUserProgressService.submitRating((long) myUser.get().getId(), (long) ratingDto.getProblemId(), ratingDto.getRating());
            return ResponseEntity.ok("{\"message\": \"Rating submitted successfully\"}"); // Send back a simple JSON message
        }
        return ResponseEntity.badRequest().body("{\"error\": \"User not found\"}");
    }

}
