package org.aleksid.wikime.service;

import org.aleksid.wikime.model.Role;
import org.aleksid.wikime.model.User;
import org.aleksid.wikime.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User getUserByName(String author) {
        return userRepo.findByUsername(author);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void saveUser(User user, String username, Map<String, String> form) {
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
    }

    public void updateProfile(User user, String password) {
        if (!password.isEmpty()) {
            user.setPassword(password);
        }
        userRepo.save(user);
    }

    public boolean addUser(User user) {
        User userFromDB = userRepo.findByUsername(user.getUsername());

        if(userFromDB != null){
            return false;
        }

        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);
        return true;
    }

    public void deleteUser(Long userId) {
        userRepo.deleteById(userId);
    }

    public boolean activate(Long userId) {
        User user = userRepo.findById(userId).orElse(null);
        if(user==null) return false;
        user.setActive(true);
        userRepo.save(user);
        return true;
    }
}
