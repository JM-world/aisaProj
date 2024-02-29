package com.aisa.survey.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aisa.survey.entity.Admin;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer>{
    Optional<Admin> findByName(String name);
}
