package com.capstone.bloodlink.repository;

import com.capstone.bloodlink.logs.BloodBankLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloodBankLoginLogRepository extends JpaRepository<BloodBankLoginLog, Long> {
}