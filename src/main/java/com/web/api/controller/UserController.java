package com.web.api.controller;

import com.web.api.dto.UsernameRequest;
import com.web.api.model.User;
import com.web.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/api/users")
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    @PostMapping("/api/register")
    public String register(@RequestBody UsernameRequest data) throws NoSuchAlgorithmException {
        System.out.println(data);
//        if(users.contains(data.getUsername())){
//            return "User Already Exist";
//        }
//        users.add(data.getUsername());
        User user = new User();
        user.setPassword(data.getPassword());
        user.setUsername(data.getUsername());
        userRepository.save(user);
        return "Successful";
    }

    @GetMapping("/api/user/{id}")
    public User getUser(@PathVariable Long id){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()){
            return null;
        }
        User user = optionalUser.get();
        return user;
    }
}
