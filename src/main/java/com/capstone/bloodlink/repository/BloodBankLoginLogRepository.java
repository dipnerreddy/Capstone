package com.capstone.bloodlink.repository;

import com.capstone.bloodlink.logs.BloodBankLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BloodBankLoginLogRepository extends JpaRepository<BloodBankLoginLog, Long> {
}
