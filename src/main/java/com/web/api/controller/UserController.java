package com.web.api.controller;

import com.web.api.dto.UserUpdateRequest;
import com.web.api.dto.UsernameRequest;
import com.web.api.dto.response.ApiResponse;
import com.web.api.model.User;
import com.web.api.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.security.NoSuchAlgorithmException;
import java.util.*;

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
    public ResponseEntity<Object> getUser(@PathVariable Long id){
        Optional<User> optionalUser = userRepository.findByIdAndDeleted(id,false);
        Map<String,Object> responseBody = new HashMap<>();
        if(optionalUser.isEmpty()){
            responseBody.put("message","User Not Found");
            responseBody.put("data",null);
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }
        User user = optionalUser.get();

        responseBody.put("message","Successful");
        responseBody.put("data",user);
        return ResponseEntity.ok(responseBody);
    }

    @PutMapping("/api/user/{id}/update")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request){
        Optional<User> optionalUser = userRepository.findByIdAndDeleted(id,false);
        ApiResponse responseBody = new ApiResponse();
        if(optionalUser.isEmpty()){
            responseBody.setMessage("Invalid User");
            responseBody.setStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(responseBody,HttpStatus.NOT_FOUND);
        }

        Optional<User> byUsername = userRepository.findByUsernameAndDeleted(request.getUsername(),false);
        if(byUsername.isPresent()){
            User user = byUsername.get();
            if(user.getId()!=id) {
                return new ResponseEntity<>("Username Already Exist",HttpStatus.BAD_REQUEST);
            }
        }

        User user = optionalUser.get();
        user.setUsername(request.getUsername());
        user.setFirstname(request.getFirstname());
        user.setOtherNames(request.getOthername());
        user.setLastname(request.getLastname());

        userRepository.save(user);
        return ResponseEntity.ok("Successful");
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
    public ResponseEntity<ApiResponse> searchByIdGreaterThan(@PathVariable Long id){
        ApiResponse responseBody = new ApiResponse();
        List<User> userList = userRepository.findByIdGreaterThanAndDeleted(id,false);
        if(userList.size()<1){
            responseBody.setData(userList);
            responseBody.setMessage("No User Found");
            responseBody.setStatus(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseBody);
        }

        responseBody.setData(userList);
        responseBody.setMessage("Successful");
        responseBody.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(responseBody);
    }

//        new User();
//        User.builder().lastname("Asas").firstname("ahsj").build();

}
