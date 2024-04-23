package org.johnnyhe.dayleet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Controller
public class ErrorPageController {
    @GetMapping("/error")
    public String handleError(Model model) {
        System.out.println("Hey Being called!");

        model.addAttribute("errorCode", "404");
        model.addAttribute("title", "Page not found");
        model.addAttribute("message", "Sorry, we couldn’t find the page you’re looking for.");
        return "error";
    }
}
