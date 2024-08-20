package com.capstone.bloodlink.controller;


import com.capstone.bloodlink.doa.BloodBankDAO;
import com.capstone.bloodlink.entity.BloodBank;
import com.capstone.bloodlink.entity.User;
import com.capstone.bloodlink.logs.BloodBankLoginLog;
import com.capstone.bloodlink.logs.LoginLog;
import com.capstone.bloodlink.repository.BloodBankLoginLogRepository;
import com.capstone.bloodlink.repository.BloodBankRepository;
import com.capstone.bloodlink.repository.BloodBankRepository1;
import com.nimbusds.jose.shaded.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bloodBank")
public class BloodBankController {
    @Autowired
    private BloodBankRepository bloodBankRepository;
    @Autowired
    private BloodBankRepository1 bloodBankRepository1;

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
    public ResponseEntity<String> UpdateBloodBank(@RequestBody BloodBank bloodBank) {

        String email = bloodBank.getEmail();

        // Fix: Ensure correct getters are used for each blood type
        int oPositive = bloodBank.getoPositive();
        int oNegative = bloodBank.getoNegative();  // This was incorrectly set to getoPositive()
        int aPositive = bloodBank.getaPositive();
        int aNegative = bloodBank.getaNegative();  // This was incorrectly set to getaPositive()
        int bPositive = bloodBank.getbPositive();
        int bNegative = bloodBank.getbNegative();  // This was incorrectly set to getbPositive()

        // Now call the DAO method to update the blood bank
        bloodBankDAO.updateBloodBank(email, oPositive, oNegative, aPositive, aNegative, bPositive, bNegative);

        return ResponseEntity.ok("Updation is done");
    }


    @GetMapping("/search")
    public ResponseEntity<String> searchBloodBank(@RequestBody BloodBank bloodBank) {
        String email = bloodBank.getEmail();
        List<BloodBank> bloodBanks = bloodBankRepository1.findByEmail(email);

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
                sanitizedData.add(sanitizedBank);
            }

            // Convert to JSON
            Gson gson = new Gson();
            String jsonData = gson.toJson(sanitizedData);

            // Clean up the data to remove "bloodBankName = " properly and avoid the trailing '='
            String replaceData = jsonData
                    .replace("\"", " ")                      // Remove quotes
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
