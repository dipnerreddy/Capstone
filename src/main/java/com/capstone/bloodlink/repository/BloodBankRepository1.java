package com.capstone.bloodlink.repository;

import com.capstone.bloodlink.entity.BloodBank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BloodBankRepository1 extends JpaRepository<BloodBank,Long> {
    List<BloodBank> findByEmail(String email);


}
