package com.capstone.bloodlink.controller;

import com.capstone.bloodlink.entity.BloodBank;
import com.capstone.bloodlink.entity.User;
import com.capstone.bloodlink.logs.BloodBankLoginLog;
import com.capstone.bloodlink.logs.LoginLog;
import com.capstone.bloodlink.repository.BloodBankLoginLogRepository;
import com.capstone.bloodlink.repository.BloodBankRepository;
import com.capstone.bloodlink.service.BloodGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/bloodBank")
public class BloodBankController {

    @Autowired
    private BloodGroupService bloodGroupService;
    @Autowired
    private BloodBankRepository bloodBankRepository;
    @Autowired
    private BloodBankLoginLogRepository bloodBankLoginLogRepository;

    @PostMapping("/login")
    public ResponseEntity<String> loginBloodBankUser(@RequestBody BloodBank bloodBank) {
        String email = bloodBank.getEmail();
        String password = bloodBank.getPassword();

        // Query the database for a blood bank with the given email
        Optional<BloodBank> bloodBankOpt = Optional.ofNullable(bloodBankRepository.findByEmail(email));

        if (bloodBankOpt.isPresent()) {
            BloodBank dbBloodBank = bloodBankOpt.get();
            if (dbBloodBank.getPassword().equals(password)) {
                // Log the successful login
                logLoginAttempt(dbBloodBank.getId(), true);
                return ResponseEntity.ok("Login successful for user: " + email);
            } else {
                // Log the failure due to incorrect password
                logLoginAttempt(dbBloodBank.getId(), false);
                return ResponseEntity.badRequest().body("Incorrect password. Please try again.");
            }
        } else {
            // Log the failure due to non-existing email
            logLoginAttempt(null, false);
            return ResponseEntity.badRequest().body("No Blood Bank is associated with given email: " + email + ". Please Contact DIPNER TECH SOLUTIONS.");
        }
    }

    private void logLoginAttempt(Long bloodBankId, boolean success) {
        String status = success ? "S" : "F";
        BloodBankLoginLog log = new BloodBankLoginLog(bloodBankId, LocalDateTime.now(), status);
        bloodBankLoginLogRepository.save(log);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateBloodGroup(
            @RequestParam int oPositive,
            @RequestParam int oNegative,
            @RequestParam int aPositive,
            @RequestParam int aNegative,
            @RequestParam int bPositive,
            @RequestParam int bNegative) {

        // Retrieve the currently authenticated blood bank
        BloodBank loggedInBloodBank = bloodGroupService.getLoggedInBloodBank();

        // Update the blood group values for the logged-in blood bank
        bloodGroupService.updateBloodGroup(loggedInBloodBank, oPositive, oNegative, aPositive, aNegative, bPositive, bNegative);

        return ResponseEntity.ok("Blood group updated successfully.");
    }
}
