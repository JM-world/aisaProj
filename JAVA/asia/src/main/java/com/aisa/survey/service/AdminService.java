package com.aisa.survey.service;

import com.aisa.survey.MemberNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aisa.survey.entity.Admin;
import com.aisa.survey.repository.AdminRepository;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AdminService {
	
	private final AdminRepository adminRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	public Admin create(String name, String password) {
		Admin admin = new Admin();
		admin.setName(name);
		admin.setPassword(passwordEncoder.encode(password));
		
		this.adminRepository.save(admin);
		return admin;
	}

}
