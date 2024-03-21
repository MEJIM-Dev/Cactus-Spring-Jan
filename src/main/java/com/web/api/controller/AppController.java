package com.web.api.controller;

import com.web.api.dto.UsernameRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@RestController
public class AppController {

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
    public ArrayList<?> users(){
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
    public String getUser (@PathVariable String id){
//        if(id>users.size() || id<1){
//            return "Invalid User";
//        }
//        return users.get(id-1);
        return encrypt(id,password);
    }

    @GetMapping("/user/{start}/{finish}")
    public List<String> getListOfUsers(@PathVariable(name = "start") int a, @PathVariable int finish){
        if(a>finish || finish>users.size() || a<1 ){
            return List.of();
        }
        return users.subList(a-1,finish);
    }

    @GetMapping("/username")
    public Object queryParameter(@RequestParam String username) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
//        if(users.indexOf(username)<0){
//            return null;
//        }
//        return users.get(users.indexOf(username));
        return decrypt(username);
    }


    @GetMapping("/v2/username")
    public Object queryParameterV2(@RequestParam int start, @RequestParam int finish){
        if(finish>users.size() || start<1 || start>finish){
            return List.of();
        }
        return users.subList(start-1,finish);
    }

    @PostMapping("/register")
    public String register(@RequestBody UsernameRequest data) throws NoSuchAlgorithmException {
        System.out.println(data);
        String encrypt = encrypt(data.getUsername(), password);
        return encrypt;
//        if(users.contains(data.getUsername())){
//            return "User Already Exist";
//        }
//        users.add(data.getUsername());
//        return "Successful";
    }

    public void genSecretKey() throws NoSuchAlgorithmException {
        SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();
        System.out.println(secretKey.toString());
    }

    @Value("secret")
    private String password;

    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void setKey(final String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String textToEncrypt, String password) {
        String encrypted = null;
        try {
            AppController.setKey(password);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            encrypted = Base64.getEncoder()
                    .encodeToString(cipher.doFinal(textToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return encrypted;
    }

    public String decrypt(String encryptedText) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        AppController.setKey(password);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes);
    }
    // Request Parameters
    // Path variable/parameters
    // Request body
}
