package com.capstone.bloodlink.controller;


import com.capstone.bloodlink.entity.User;
import com.capstone.bloodlink.logs.LoginLog;
import com.capstone.bloodlink.repository.LoginLogRepository;
import com.capstone.bloodlink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginLogRepository loginLogRepository;



    // this is add user method
    @PostMapping("/addUser")
    public ResponseEntity<String> createUser(@RequestBody User user){

        String mobile=user.getMobileNo();
        User isPresent = userRepository.findByMobileNo(mobile);
        if (isPresent != null) {
            // If user is found with the same mobile number, return a message to log in
            return ResponseEntity.ok("USER IS ALREADY REGISTERED WITH MOBILE NUMBER: " + mobile + ". PLEASE GO TO LOGIN PAGE AND LOGIN THERE.");
        } else {
            // If no user is found with the same mobile number, create a new user
            User newUser = userRepository.save(user);
            return ResponseEntity.ok("USER {" + newUser.getUsername() + "} WITH ID {" + newUser.getId() + "} IS SAVED TO DATABASE.");
        }
    }

    // this is a login method
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User loginUser) {
        // Extract mobile number and password from the User object
        String mobile = loginUser.getMobileNo();
        String password = loginUser.getPassword();

        // Query the database for a user with the given mobile number
        User user = userRepository.findByMobileNo(mobile);

        // Record the login attempt with the current timestamp
        LocalDateTime loginTime = LocalDateTime.now();
        String status;

        if (user == null) {
            // If user is not found, log the failure
            status = "F";
            loginLogRepository.save(new LoginLog(null, loginTime, status));
            return ResponseEntity.badRequest().body("No account associated with mobile number: " + mobile + ". Please register.");
        } else {
            // If user exists, check if the password matches
            if (user.getPassword().equals(password)) {
                // Log the successful login
                status = "S";
                loginLogRepository.save(new LoginLog(user.getId(), loginTime, status));
                return ResponseEntity.ok("Login successful for user: " + user.getUsername());
            } else {
                // Log the failure due to incorrect password
                status = "F";
                loginLogRepository.save(new LoginLog(user.getId(), loginTime, status));
                return ResponseEntity.badRequest().body("Incorrect password. Please try again.");
            }
        }
    }




}
