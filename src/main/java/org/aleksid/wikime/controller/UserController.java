package org.aleksid.wikime.controller;

import org.aleksid.wikime.model.Role;
import org.aleksid.wikime.model.User;
import org.aleksid.wikime.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")//class mapping
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public String userLIst(Model model){
        List<User> users = userRepo.findAll();
        model.addAttribute("users", users);
        return "userList";
    }
    @GetMapping("{user}")//хитрый мэппинг
    public String userEditForm(
            @PathVariable User user,
            Model model){
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form,//вообще всё
            @RequestParam("userId") User user
    ){
        user.setUsername(username);

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());//строковые представления ролей

        user.getRoles().clear();//очищаем все роли пользователя
        for (String k : form.keySet()){//в том числе по ключу на каждую выбранную чекбоксом роль
            if(roles.contains(k)){
                user.getRoles().add(Role.valueOf(k));
            }
        }

            userRepo.save(user);
        return "redirect:/user";
    }
}
