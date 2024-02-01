package com.aisa.survey.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	// 첫번째 [시작화면]
	@GetMapping("/")
	public String main() {
		return "main";
	}
	
	// 두번째 [선택화면]
	@GetMapping("/select")
	public String select() {
		return "select";
	}
	
	// 세번째 [설문화면]
	@GetMapping("/survey")
	public String survey() {
		return "survey";
	}
	

}
