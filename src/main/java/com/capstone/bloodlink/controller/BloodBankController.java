package com.capstone.bloodlink.controller;


import com.capstone.bloodlink.doa.BloodBankDAO;
import com.capstone.bloodlink.entity.BloodBank;
import com.capstone.bloodlink.entity.User;
import com.capstone.bloodlink.logs.BloodBankLoginLog;
import com.capstone.bloodlink.logs.LoginLog;
import com.capstone.bloodlink.repository.BloodBankLoginLogRepository;
import com.capstone.bloodlink.repository.BloodBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/bloodBank")
public class BloodBankController {
    @Autowired
    private BloodBankRepository bloodBankRepository;

    @Autowired
    private BloodBankLoginLogRepository bloodBankLoginLogRepository;

    @Autowired
    private BloodBankDAO bloodBankDAO;

    @PostMapping("/login")
    public ResponseEntity<String> loginBloodBank(@RequestBody BloodBank bloodBank) {
        // Extract mobile number and password from the User object
        String email = bloodBank.getEmail();
        String password = bloodBank.getPassword();

        // Query the database for a user with the given mobile number
        BloodBank user = bloodBankRepository.findByEmail(email);

        // Record the login attempt with the current timestamp
        LocalDateTime loginTime = LocalDateTime.now();
        String status;

        if (user == null) {
            // If user is not found, log the failure
            status = "F";
            bloodBankLoginLogRepository.save(new BloodBankLoginLog(null, loginTime, status));
            return ResponseEntity.badRequest().body("No account associated with mobile number: " + email + ". Please register.");
        } else {
            // If user exists, check if the password matches
            if (user.getPassword().equals(password)) {
                // Log the successful login
                status = "S";
                bloodBankLoginLogRepository.save(new BloodBankLoginLog(user.getId(), loginTime, status));
                return ResponseEntity.ok("Login successful for user: " + user.getBloodBankName());
            } else {
                // Log the failure due to incorrect password
                status = "F";
                bloodBankLoginLogRepository.save(new BloodBankLoginLog(user.getId(), loginTime, status));
                return ResponseEntity.badRequest().body("Incorrect password. Please try again.");
            }
        }
    }


    @PostMapping("/update")
    public ResponseEntity<String> UpdateBloodBank(@RequestBody BloodBank bloodBank){

        String email = bloodBank.getEmail();
        int oPositive=bloodBank.getoPositive();
        int oNegative=bloodBank.getoPositive();
        int aPositive=bloodBank.getaPositive();
        int aNegative=bloodBank.getaPositive();
        int bPositive=bloodBank.getbPositive();
        int bNegative=bloodBank.getbPositive();

//        BloodBank update = bloodBankRepository.save(new BloodBank(email,oPositive,oNegative,aPositive,aNegative,bPositive,bNegative));

        bloodBankDAO.updateBloodBank(email,oPositive,oNegative,aPositive,aNegative,bPositive,bNegative);
        return ResponseEntity.ok("Updation is done");

    }
}
