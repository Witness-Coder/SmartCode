package com.example.SmartCode.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.SmartCode.entityClasses.ResetToken;

public interface TokenRepository extends JpaRepository<ResetToken, Long> {
    
    Optional<ResetToken> findByToken(String token);
    Optional<ResetToken> findByManager_Email(String email);
    Optional<ResetToken> findByCustomer_Email(String email);

}
