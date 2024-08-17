package com.capstone.bloodlink.repository;

import com.capstone.bloodlink.entity.BloodBank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


//public interface BloodBankRepository extends JpaRepository<BloodBank, Long> {
//
//    BloodBank findByName(String name);
//}

public interface BloodBankRepository extends JpaRepository<BloodBank, Long> {
    Optional<BloodBank> findByName(String username); // Replace 'username' with the correct field name
    BloodBank findByEmail(String email);
}