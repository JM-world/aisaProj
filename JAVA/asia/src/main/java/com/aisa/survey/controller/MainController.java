package com.aisa.survey.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.aisa.survey.Service.AnswerService;
import com.aisa.survey.dto.InfoRequestDto;
import com.aisa.survey.entity.Question;
import com.aisa.survey.repository.QuestionRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MainController {
	
	
	private final QuestionRepository questionRepository;
	
	private final AnswerService answerService;
	
	// 첫번째 [시작화면]
	@GetMapping("/")
	public String main(HttpSession session) {
		session.invalidate();
		return "main";
	}
	
	// 두번째 [선택화면]
	@GetMapping("/select")
	public String select(Model model, InfoRequestDto requestDto) {
		model.addAttribute("requestDto", requestDto);
		return "select";
	}
	
	// 임시 id 저장
	int sessionId = 0;
	// 두번째 [성별, 나이 입력 후 DB 저장]
	@PostMapping("/select")
	public String selectPost(HttpSession session, @ModelAttribute("requestDto") InfoRequestDto requestDto) {
		int id = this.answerService.create(requestDto.getGender(), requestDto.getAge());
		this.sessionId = id;
		session.setAttribute("surveyId", id);
		return "redirect:/survey";
	}
	
	
	// 세번째 [설문화면]
	@GetMapping("/survey")
	public String survey(Model model) {
		List<Question> questionList = this.questionRepository.findAll();
		
		model.addAttribute("questionList", questionList);
		return "survey";
	}
	
	String obj = "";
	// 세번째 [제출 시]
	@PostMapping("/survey")
	public String surveyPost(@RequestParam("1") String a1, @RequestParam("2") String a2,
						  @RequestParam("3") String a3, @RequestParam("4") String a4,
						  @RequestParam("5") String a5, @RequestParam("6") String a6,
						  HttpSession session, Model model) throws URISyntaxException, ParseException {
		this.answerService.update(this.sessionId, a1, a2, a3, a4, a5, a6);
		
		String apiUrl = "http://192.168.0.24:8000/create/answer";
        URI uri = new URI(apiUrl);

        // parameter setting
        HashMap<String, Integer> body = new HashMap<>();
        body.put("userId", this.sessionId);
        body.put("Q1", Integer.parseInt(a1));
        body.put("Q2", Integer.parseInt(a2));
        body.put("Q3", Integer.parseInt(a3));
        body.put("Q4", Integer.parseInt(a4));
        body.put("Q5", Integer.parseInt(a5));
        body.put("Q6", Integer.parseInt(a6));

        body.put("elapsedTime", 20);
        // body.put("uid", uid); 등 정보를 계속 보낼 수 있음
        HttpEntity<HashMap<String, Integer>> entity = new HttpEntity<>(body, new HttpHeaders());

        // 정보를 주고 받음
        RestTemplate rest = new RestTemplate();
        ResponseEntity<String> response = rest.exchange(uri, HttpMethod.POST, entity, String.class);
        System.out.println(response.getBody());
	    JSONParser json = new JSONParser();
	    JSONObject obj = (JSONObject) json.parse(response.getBody().toString());
	    model.addAttribute("obj", obj.get("Answer").toString());
	    model.addAttribute("abc", "abc");
	    System.out.println(obj.get("Answer").toString());
	    this.obj = obj.get("Answer").toString();
		
		return "redirect:/result";
		
	}
	
	// 네번째 [결과화면]
	@GetMapping("/result")
	public String result(Model model) {
		model.addAttribute("obj", this.obj);
		return "result";
	}
	

}
