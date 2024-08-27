package com.capstone.bloodlink.controller;

import com.capstone.bloodlink.doa.BloodBankDAO;
import com.capstone.bloodlink.entity.BloodBank;
import com.capstone.bloodlink.entity.PasswordResetRequest;
import com.capstone.bloodlink.entity.User;
import com.capstone.bloodlink.logs.LoginLog;
import com.capstone.bloodlink.repository.BloodBankRepository;
import com.capstone.bloodlink.repository.LoginLogRepository;
import com.capstone.bloodlink.repository.PasswordResetRequestRepository;
import com.capstone.bloodlink.repository.UserRepository;
import com.nimbusds.jose.shaded.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/UserController")
public class UserController {

    @Autowired
    private BloodBankDAO bloodBankDAO;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginLogRepository loginLogRepository;

    @Autowired
    private PasswordResetRequestRepository passwordResetRequestRepository;
    @Autowired
    private BloodBankRepository bloodBankRepository;

    private static final SecureRandom random = new SecureRandom();
    private String jsonData;

    @PostMapping("/addUser")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        // Validate input
        String username = user.getUsername();
        String password = user.getPassword();
        String mobile = user.getMobileNo();

        if (username == null || username.isEmpty() ||
                password == null || password.isEmpty() ||
                mobile == null || mobile.isEmpty()) {
            return ResponseEntity.badRequest().body("All fields (username, password, mobileNo) are required.");
        }

        // Check if user with the same mobile number already exists
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


    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User loginUser) {
        try {
            String mobile = loginUser.getMobileNo();
            String password = loginUser.getPassword();
            User user = userRepository.findByMobileNo(mobile);
            LocalDateTime loginTime = LocalDateTime.now();
            String status;

            if (user == null) {
                status = "F";
                loginLogRepository.save(new LoginLog(null, loginTime, status));
                return ResponseEntity.badRequest().body("No account associated with mobile number: " + mobile + ". Please register.");
            } else {
                if (user.getPassword().equals(password)) {
                    status = "S";
                    loginLogRepository.save(new LoginLog(user.getId(), loginTime, status));
                    return ResponseEntity.ok("Login successful for user: " + user.getUsername());
                } else {
                    status = "F";
                    loginLogRepository.save(new LoginLog(user.getId(), loginTime, status));
                    return ResponseEntity.badRequest().body("Incorrect password. Please try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }


//    @PostMapping("/forgotPassword")
//    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
//        String mobileNo = request.get("mobileNo");
//        String otp="1234";
//
//        User user = userRepository.findByMobileNo(mobileNo);
//        if (user == null) {
//            return ResponseEntity.badRequest().body("User does not exist. Please register.");
//        }
//
//        // Generate OTP
////        String otp = String.format("%04d", random.nextInt(10000));
//
//        // Store OTP in the database
//        PasswordResetRequest resetRequest = new PasswordResetRequest(mobileNo, otp, LocalDateTime.now());
//        passwordResetRequestRepository.save(resetRequest);
//        return ResponseEntity.ok("OTP sent to mobile number: " + mobileNo);
//    }

//    @PostMapping("/resetPassword")
//    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
//        String mobileNo = request.get("mobileNo");
//        String otp = request.get("otp");
//        String newPassword = request.get("newPassword");
//        String confirmPassword = request.get("confirmPassword");
//
//        PasswordResetRequest resetRequest = passwordResetRequestRepository.findByMobileNo(mobileNo);
//        if (resetRequest == null) {
//            return ResponseEntity.badRequest().body("Invalid request. OTP not found.");
//        }
//
//        // Check OTP validity
//        if (!resetRequest.getOtp().equals(otp)) {
//            return ResponseEntity.badRequest().body("Incorrect OTP. Please try again.");
//        }
//
//        // Validate new passwords
//        if (!newPassword.equals(confirmPassword)) {
//            return ResponseEntity.badRequest().body("Passwords do not match.");
//        }
//
//        // Update user's password
//        User user = userRepository.findByMobileNo(mobileNo);
//        if (user != null) {
//            user.setPassword(newPassword); // Ensure you hash the password before saving
//            userRepository.save(user);
//        }
//
//        // Delete OTP request after successful reset
//        passwordResetRequestRepository.delete(resetRequest);
//
//        return ResponseEntity.ok("Password reset successful.");
//    }

    @PostMapping("/forgetpassword")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String mobileNo = request.get("mobileNo");

        User user = userRepository.findByMobileNo(mobileNo);
        if (user == null) {
            return ResponseEntity.badRequest().body("User does not exist. Please register.");
        }

        PasswordResetRequest resetRequest = new PasswordResetRequest(mobileNo, LocalDateTime.now());
        passwordResetRequestRepository.save(resetRequest);
        return ResponseEntity.ok("OTP sent");
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> request){
        String mobileNo=request.get("mobileNo");
        int otp=1234;
        String optEntered=request.get("otp");
        int otp1=Integer.parseInt(optEntered);

        if(otp==otp1){
            return ResponseEntity.ok("OTP verified'");
        }
        else{
            return (ResponseEntity<String>) ResponseEntity.badRequest();
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request){
        String mobileNo = request.get("mobileNo");
        String newPassword = request.get("newPassword");
        String confirmPassword = request.get("confirmPassword");


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

        return ResponseEntity.ok("Password reset successfully");

    }





    @GetMapping("/search")
    public ResponseEntity<String> searchBloodBank(@RequestBody BloodBank bloodBank) {
        String location = bloodBank.getLocation();
        List<BloodBank> bloodBanks = bloodBankRepository.findByLocation(location);

        if (!bloodBanks.isEmpty()) {
            // Create a sanitized list to hold only the required fields in correct order
            List<Map<String, Object>> sanitizedData = new ArrayList<>();

            for (BloodBank bank : bloodBanks) {
                Map<String, Object> sanitizedBank = new LinkedHashMap<>(); // Use LinkedHashMap to maintain order
                sanitizedBank.put("O+", bank.getoPositive());
                sanitizedBank.put("O-", bank.getoNegative());
                sanitizedBank.put("A+", bank.getaPositive());
                sanitizedBank.put("A-", bank.getaNegative());
                sanitizedBank.put("B+", bank.getbPositive());
                sanitizedBank.put("B-", bank.getbNegative());
                sanitizedBank.put("bloodBankName", bank.getBloodBankName());
                sanitizedData.add(sanitizedBank);
            }

            // Convert to JSON
            Gson gson = new Gson();
            String jsonData = gson.toJson(sanitizedData);

            // Clean up the data to remove "bloodBankName = " properly and avoid the trailing '='
            String replaceData = jsonData
                    .replace("\"", " ")                      // Remove quotes
                    .replace("bloodBankName :", "")           // Remove the key "bloodBankName :"
                    .replace(" = ", " ")                     // Clean up any leftover equal signs
                    .replace(" ,", ",")                      // Handle commas after removal
                    .replace("},{", "}\r\n{")                // Format to print each bank on a new line
                    .replace(":", "= ")                      // Convert colon to equal for formatting
                    .replace("[", " ")                       // Clean up brackets
                    .replace("]", " ");                      // Clean up brackets

            return ResponseEntity.ok(replaceData.trim());
        } else {
            return ResponseEntity.ok("The App Service is not available in that location");
        }
    }








}
