package org.johnnyhe.dayleet.controller;

import org.johnnyhe.dayleet.model.*;
import org.johnnyhe.dayleet.repository.*;
import org.johnnyhe.dayleet.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class AuthController {
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final userRepo myUserRepo;
    private final questionListRepo myQuestionListRepo;

    private final questionRepo myQuestionRepo;

    private final questionPlaceholderRepo myQuestionPlaceHolderRepo;

    private final codingLangRepo myCodingLangRepo;

    @Autowired
    public AuthController(UserDetailsServiceImpl userDetailsService, PasswordEncoder passwordEncoder, userRepo myUserRepo, questionListRepo myQuestionListRepo, questionRepo myQuestionRepo, questionPlaceholderRepo myQuestionPlaceHolderRepo, codingLangRepo myCodingLangRepo) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.myUserRepo = myUserRepo;
        this.myQuestionListRepo = myQuestionListRepo;
        this.myQuestionRepo = myQuestionRepo;
        this.myQuestionPlaceHolderRepo = myQuestionPlaceHolderRepo;
        this.myCodingLangRepo = myCodingLangRepo;
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
}
