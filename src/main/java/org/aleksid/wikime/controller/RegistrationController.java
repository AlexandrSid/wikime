package org.aleksid.wikime.controller;

import org.aleksid.wikime.kafka.KafkaService;
import org.aleksid.wikime.model.User;
import org.aleksid.wikime.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    private static final Logger logger = LogManager.getLogger(MainController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private KafkaService kafkaService;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {

        if (!userService.addUser(user)) {

            logger.warn(String.format("Attempting of register with existing username %s", user.getUsername()));

            model.addAttribute("usernameError", "User exists");
            return "registration";

        }

        //TODO
        //send the notification to admins here.
        kafkaService.registrationAnnouncement(user);

        logger.info(String.format("User %s has been registered", user.getUsername()));

        return "redirect:/login";
    }
}
