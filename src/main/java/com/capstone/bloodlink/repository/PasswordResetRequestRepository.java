package com.capstone.bloodlink.repository;

import com.capstone.bloodlink.entity.PasswordResetRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetRequestRepository extends JpaRepository<PasswordResetRequest, Long> {
    PasswordResetRequest findByMobileNo(String mobileNo);
}
