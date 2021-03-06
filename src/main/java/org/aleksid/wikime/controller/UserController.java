package org.aleksid.wikime.controller;

import org.aleksid.wikime.model.Role;
import org.aleksid.wikime.model.User;
import org.aleksid.wikime.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")//class mapping
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

    @Autowired
    private UserService userService;

    private static final Logger logger = LogManager.getLogger(MainController.class);

    @GetMapping
    public String userLIst(
            Model model,
            @RequestParam(required = false, defaultValue = "") String usrD,
            @RequestParam(required = false, defaultValue = "") String usrA
    ){
        if(!usrD.equals("")){
            model.addAttribute("deletedUserName", usrD);
        }
        if(!usrA.equals("")){
            model.addAttribute("activatedUserName", usrA);
        }
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "userList";
    }

    @GetMapping("{user}")//хитрый мэппинг
    public String userEditForm(
            @PathVariable User user,
            Model model){

        logger.info(String.format("Requested user id = %d", user.getId()));

        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form,//вообще всё, что было в форме
            @RequestParam("userId") User user
    ){
        userService.saveUser(user, username, form);

        logger.info(String.format("User id = %d has been changed", user.getId()));

        return "redirect:/user";
    }

    @PostMapping("delete")
    public String deleteUser(
            @RequestParam Long userId,
            @RequestParam String username,
            RedirectAttributes redirectAttributes
    ) {
        userService.deleteUser(userId);

        //можно было бы записать deleted by AuthenticationPrincipal user.userName, но стоит ли ради этого добавлять не несущие другой функциональности аргументы
        logger.warn(String.format("User id = %d has been deleted", userId));

        redirectAttributes.addAttribute("usrD", username);
        return "redirect:/user";
    }

    @PostMapping("activate")
    public String activateUser(
            @RequestParam Long userId,
            @RequestParam String username,
            RedirectAttributes redirectAttributes) {
        userService.activate(userId);

        logger.warn(String.format("User id = %d has been ACTIVATED", userId));

        redirectAttributes.addAttribute("usrA", username);
        return "redirect:/user";
    }

    @GetMapping("profile")
    @PreAuthorize("hasAnyAuthority('USER', 'MODERATOR')")
    public String getProfile(
            Model model,
            @AuthenticationPrincipal User user,
            @RequestParam(required = false, defaultValue = "") String pwd) {//не придумал как лучше передать  сообщение об успешной смене пароля
        model.addAttribute("username", user.getUsername());
        if (pwd.equals("chgd")) {
            model.addAttribute("password_changed", "");
        }
        return "profile";
    }

    @PostMapping("profile")
    @PreAuthorize("hasAnyAuthority('USER', 'MODERATOR')")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam String password,
            RedirectAttributes redirectAttributes
    ){
        userService.updateProfile(user, password);

        logger.warn(String.format("Requested user id = %d (%s) changed his password", user.getId(), user.getUsername()));

        redirectAttributes.addAttribute("pwd", "chgd");//короткие названия/значения, так как пойдут в URL
        return "redirect:/user/profile";
    }
}
