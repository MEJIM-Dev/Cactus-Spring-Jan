package com.web.api.controller;

import com.web.api.model.User;
import com.web.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class AppController {

    @Autowired
    private UserRepository userRepository;

    int as = 12;

    ArrayList<String> users = new ArrayList<>(Arrays.asList("User101","sanBean","kellyFix","FreddyKg"));

    @RequestMapping("/") // "localhost:8080/ or localhost:8080"
    public String homepage(){
        return "Welcome";
    }

    @RequestMapping(value = "/about")
    public String about(){
        return "About";
    }

    @RequestMapping("/users-count")
    public int getUsersCount(){
        System.out.println(users.size());
        return users.size();
    }

    @RequestMapping("/users-exist")
    public boolean usersExist(){
        return !users.isEmpty();
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> users(){
        List<User> users = userRepository.findAll();
        return users;
    }

    @GetMapping("/get-end")
    public String getEndpoint(){
        return "get";
    }

    @RequestMapping(value = "/req-get-end", method = RequestMethod.GET)
    public String reqGetEndpoint(){
        return "get";
    }

    @PostMapping("/users")
    public String postUser(){
        return "Post User";
    }

    @GetMapping("/user/{id}")
    public String getUser (@PathVariable int id){
        if(id>users.size() || id<1){
            return "Invalid User";
        }
        return users.get(id-1);
    }

    @GetMapping("/user/{start}/{finish}")
    public List<String> getListOfUsers(@PathVariable(name = "start") int a, @PathVariable int finish){
        if(a>finish || finish>users.size() || a<1 ){
            return List.of();
        }
        return users.subList(a-1,finish);
    }

    @GetMapping("/username")
    public Object queryParameter(@RequestParam String username) {
        if(users.indexOf(username)<0){
            return null;
        }
        return users.get(users.indexOf(username));
    }


    @GetMapping("/v2/username")
    public Object queryParameterV2(@RequestParam int start, @RequestParam int finish){
        if(finish>users.size() || start<1 || start>finish){
            return List.of();
        }
        return users.subList(start-1,finish);
    }

    // Request Parameters
    // Path variable/parameters
    // Request body
}
