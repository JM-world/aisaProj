package com.aisa.survey.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aisa.survey.form.AdminCreateForm;
import com.aisa.survey.service.AdminService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {
	
	private final AdminService adminService;
	
	@Value("${KEY.CODE}")
	private String KEY_CODE;
	
	@GetMapping("/signup")
	public String signup(AdminCreateForm adminCreateForm) {
		return "admin/signup_form";
	}
	
	@PostMapping("signup")
	public String signup(@Valid AdminCreateForm adminCreateForm, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "admin/signup_form";
		}
		
		if(!adminCreateForm.getPassword1().equals(adminCreateForm.getPassword2())) {
			bindingResult.rejectValue("password2", "passwordInCorrect",
					"패스워드가 서로 일치하지 않습니다.");
			return "/admin/signup_form";
		}


		if(!adminCreateForm
				.getKeyCode().equals(this.KEY_CODE)) {
			bindingResult.rejectValue("keyCode", "keyCodeInCorrect",
					"잘못된 방식의 ADMIN KEY입니다.");
			return "/admin/signup_form";

		}
		
//		adminService.create(adminCreateForm.getName(), adminCreateForm.getPassword1());
		
		return "redirect:/admin";
	}
}
