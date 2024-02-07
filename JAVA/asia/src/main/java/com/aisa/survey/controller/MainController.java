package com.aisa.survey.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aisa.survey.Service.QuestionService;
import com.aisa.survey.entity.Question;
import com.aisa.survey.repository.QuestionRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MainController {
	
	@Autowired
	private final QuestionRepository questionRepository;
	
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
	public String survey(Model model) {
		List<Question> questionList = this.questionRepository.findAll();
		model.addAttribute("questionList", questionList);
		return "survey";
	}
	

}
