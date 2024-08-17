package com.capstone.bloodlink.repository;

import com.capstone.bloodlink.entity.BloodBank;
import com.capstone.bloodlink.entity.BloodGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BloodGroupRepository extends JpaRepository<BloodGroup, Long> {
    Optional<BloodGroup> findByBloodBank(BloodBank bloodBank);
}