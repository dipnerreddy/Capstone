package com.capstone.bloodlink.service;

import com.capstone.bloodlink.entity.BloodBank;
import com.capstone.bloodlink.entity.BloodGroup;
import com.capstone.bloodlink.repository.BloodBankRepository;
import com.capstone.bloodlink.repository.BloodGroupRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class BloodGroupService {

    @Autowired
    private BloodGroupRepository bloodGroupRepository;

    @Autowired
    private BloodBankRepository bloodBankRepository;

    // Retrieve the currently logged-in blood bank
    public BloodBank getLoggedInBloodBank() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // Fetch username of logged-in blood bank

        // Fetch the BloodBank entity from the repository
        return bloodBankRepository.findByName(username)
                .orElseThrow(() -> new RuntimeException("Blood bank not found"));
    }

    // Update the blood group for the logged-in blood bank
    @Transactional
    public void updateBloodGroup(BloodBank bloodBank, int oPositive, int oNegative, int aPositive, int aNegative, int bPositive, int bNegative) {
        BloodGroup bloodGroup = bloodGroupRepository.findByBloodBank(bloodBank)
                .orElseThrow(() -> new RuntimeException("Blood group not found"));

        bloodGroup.setoPositive(oPositive);
        bloodGroup.setoNegative(oNegative);
        bloodGroup.setaPositive(aPositive);
        bloodGroup.setaNegative(aNegative);
        bloodGroup.setbPositive(bPositive);
        bloodGroup.setbNegative(bNegative);

        bloodGroupRepository.save(bloodGroup);
    }
}
