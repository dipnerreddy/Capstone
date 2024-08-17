package com.capstone.bloodlink.controller;


import com.capstone.bloodlink.entity.BloodBank;
import com.capstone.bloodlink.entity.User;
import com.capstone.bloodlink.repository.BloodBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private BloodBankRepository bloodBankRepository;

    @PostMapping("/addBloodBank")
    public ResponseEntity<String> createUser(@RequestBody BloodBank bloodBank){

        String email=bloodBank.getEmail();
        BloodBank isPresent = bloodBankRepository.findByEmail(email);
        if (isPresent != null) {
            // If user is found with the same mobile number, return a message to log in
            return ResponseEntity.ok("BLOOD BANK IS ALREADY REGISTERED WITH THIS EMAIL: " + email + ". PLEASE GO TO LOGIN PAGE AND LOGIN THERE.");
        } else {
            // If no user is found with the same mobile number, create a new user
            BloodBank newUser = bloodBankRepository.save(bloodBank);
            return ResponseEntity.ok("USER {" + newUser.getName() + "} WITH ID {" + newUser.getId() + "} IS SAVED TO DATABASE.");
        }
    }
}
