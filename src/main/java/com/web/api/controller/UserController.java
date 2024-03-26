package com.web.api.controller;

import com.web.api.dto.UserUpdateRequest;
import com.web.api.dto.UsernameRequest;
import com.web.api.model.User;
import com.web.api.repository.UserRepository;
import jakarta.validation.Valid;
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
        return userRepository.findAllByDeleted(false);
    }

    @PostMapping("/api/register")
    public String register(@Valid @RequestBody UsernameRequest data) throws NoSuchAlgorithmException {
        System.out.println(data);

        Optional<User> optionalUser = userRepository.findByUsernameAndDeleted(data.getUsername(),false);
        if(optionalUser.isPresent()){
            return "User Already Exist";
        }

        User user = new User();
        user.setPassword(data.getPassword());
        user.setUsername(data.getUsername());
        userRepository.save(user);
        return "Successful";
    }

    @GetMapping("/api/user/{id}")
    public User getUser(@PathVariable Long id){
        Optional<User> optionalUser = userRepository.findByIdAndDeleted(id,false);
        if(optionalUser.isEmpty()){
            return null;
        }
        User user = optionalUser.get();
        return user;
    }

    @PutMapping("/api/user/{id}/update")
    public Object updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request){
        Optional<User> optionalUser = userRepository.findByIdAndDeleted(id,false);
        if(optionalUser.isEmpty()){
            return "Invalid User";
        }

        Optional<User> byUsername = userRepository.findByUsernameAndDeleted(request.getUsername(),false);
        if(byUsername.isPresent()){
            User user = byUsername.get();
            if(user.getId()!=id) {
                return "Username Already Exist";
            }
        }

        User user = optionalUser.get();
        user.setUsername(request.getUsername());
        user.setFirstname(request.getFirstname());
        user.setOtherNames(request.getOthername());
        user.setLastname(request.getLastname());

        userRepository.save(user);
        return "Successful";
    }

    @DeleteMapping("/api/user/{id}/hard-delete")
    public Object hardDeleteUser(@PathVariable Long id){
        boolean existsById = userRepository.existsByIdAndDeleted(id,false);
        if(!existsById){
            return "Invalid User";
        }
        userRepository.deleteById(id);
        return "Successful";
    }

    @DeleteMapping("/api/user/{id}/soft-delete")
    public Object deleteUser(@PathVariable Long id){
        Optional<User> optionalUser = userRepository.findByIdAndDeleted(id,false);
        if(optionalUser.isEmpty()){
            return "Invalid User";
        }
        User user = optionalUser.get();
        user.setDeleted(true);
        userRepository.save(user);

        return "Successful";
    }

    @GetMapping("/api/users/search/{id}")
    public Object searchByIdGreaterThan(@PathVariable Long id){
        List<User> userList = userRepository.findByIdGreaterThanAndDeleted(id,false);
        return userList;
    }

//        new User();
//        User.builder().lastname("Asas").firstname("ahsj").build();

}
