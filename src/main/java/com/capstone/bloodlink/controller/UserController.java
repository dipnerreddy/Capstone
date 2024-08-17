package com.capstone.bloodlink.controller;


import com.capstone.bloodlink.entity.PasswordResetRequest;
import com.capstone.bloodlink.entity.User;
import com.capstone.bloodlink.logs.LoginLog;
import com.capstone.bloodlink.repository.LoginLogRepository;
import com.capstone.bloodlink.repository.PasswordResetRequestRepository;
import com.capstone.bloodlink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/userRepository")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginLogRepository loginLogRepository;

    @Autowired
    private PasswordResetRequestRepository passwordResetRequestRepository;

    private static final SecureRandom random = new SecureRandom();

    // this is add user method
    //checking also done.
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

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String mobileNo = request.get("mobileNo");

        User user = userRepository.findByMobileNo(mobileNo);
        if (user == null) {
            return ResponseEntity.badRequest().body("User does not exist. Please register.");
        }

        // Generate OTP
        String otp = String.format("%04d", random.nextInt(10000));

        // Store OTP in the database
        PasswordResetRequest resetRequest = new PasswordResetRequest(mobileNo, otp, LocalDateTime.now());
        passwordResetRequestRepository.save(resetRequest);

        // Send OTP to user (you need to implement this)
        // sendOtpToUser(mobileNo, otp);

        return ResponseEntity.ok("OTP sent to mobile number: " + mobileNo);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String mobileNo = request.get("mobileNo");
        String otp = request.get("otp");
        String newPassword = request.get("newPassword");
        String confirmPassword = request.get("confirmPassword");

        PasswordResetRequest resetRequest = passwordResetRequestRepository.findByMobileNo(mobileNo);
        if (resetRequest == null) {
            return ResponseEntity.badRequest().body("Invalid request. OTP not found.");
        }

        // Check OTP validity
        if (!resetRequest.getOtp().equals(otp)) {
            return ResponseEntity.badRequest().body("Incorrect OTP. Please try again.");
        }

        // Validate new passwords
        if (!newPassword.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body("Passwords do not match.");
        }

        // Update user's password
        User user = userRepository.findByMobileNo(mobileNo);
        if (user != null) {
            user.setPassword(newPassword); // Ensure you hash the password before saving
            userRepository.save(user);
        }

        // Delete OTP request after successful reset
        passwordResetRequestRepository.delete(resetRequest);

        return ResponseEntity.ok("Password reset successful.");
    }


}
