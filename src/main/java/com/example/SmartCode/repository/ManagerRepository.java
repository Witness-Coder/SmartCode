package com.example.SmartCode.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SmartCode.entityClasses.Manager;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Manager findByEmail(String email);
    
}
