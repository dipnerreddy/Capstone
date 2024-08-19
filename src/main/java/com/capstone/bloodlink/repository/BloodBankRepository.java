package com.capstone.bloodlink.repository;

import com.capstone.bloodlink.entity.BloodBank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BloodBankRepository extends JpaRepository<BloodBank,Long> {
    BloodBank findByEmail(String email);
    List<BloodBank> findByLocation(String location);


}
