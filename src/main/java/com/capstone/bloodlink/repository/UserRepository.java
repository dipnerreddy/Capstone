package com.capstone.bloodlink.repository;

import com.capstone.bloodlink.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByMobileNo(String mobileNo);
}
