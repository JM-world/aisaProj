package com.aisa.survey.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.aisa.survey.entity.AdminPage;
import com.aisa.survey.repository.AdminPageRepository;
import com.aisa.survey.repository.AdminRepository;
import com.aisa.survey.service.AdminPageService;
import com.aisa.survey.service.AdminService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.aisa.survey.dto.InfoRequestDto;
import com.aisa.survey.entity.Question;
import com.aisa.survey.repository.QuestionRepository;
import com.aisa.survey.service.AnswerService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MainController {
	
	
	private final QuestionRepository questionRepository;
	
	private final AnswerService answerService;

	private final AdminPageService adminPageService;
	private final AdminPageRepository adminpageRepository;

	String obj = "";

	// 오늘 날짜
	private static String TODAY = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd"));
	
	// 설문 시간 측
	private static Instant stime;
	private static Instant etime;
	
	// 첫번째 [시작화면]
	@GetMapping("/main")
	public String mainPage(HttpSession session) {
		this.obj = "";
		// 방문자 수 카운트 -> 어드민 페이지에서 사용
		Optional<AdminPage> adminPage = this.adminpageRepository.findByDate(this.TODAY);
		if (adminPage.isPresent()) {
			AdminPage adminPage1 = adminPage.get();
			adminPage1.setVisitCount(adminPage1.getVisitCount() + 1);
			this.adminpageRepository.save(adminPage1);
		} else {
			this.adminPageService.create(this.TODAY);
		}

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
		return "redirect:/survey/1";
	}

	
	// 세번째 [설문화면]
	@GetMapping("/survey/{page}")
	public String getQuestionSetById(@PathVariable("page") int page, Model model) {
		// 시작 시간 체크
		stime = Instant.now();
		
	    List<Question> questionList = new ArrayList<>();
	    if (page == 1) {
	    	questionList = this.questionRepository.findByQuestionIdBetween(1, 6);
		} else if (page == 2) {
	        questionList = this.questionRepository.findByQuestionIdBetween(7, 15);
	    } else if (page == 3) {
	        questionList = this.questionRepository.findByQuestionIdBetween(16, 21);
	    }
	    
	    // 현재 사용자가 표시할 질문 세트를 모델에 추가합니다.
	    page += 1;
	    model.addAttribute("page", page);
	    model.addAttribute("questionList", questionList);
//	    if (page == 4) {
//	    	page = 2;
//	    }

		// 각 페이지가 로딩될 때마다 로딩 바를 보이게 합니다.
		model.addAttribute("showLoadingBar", true);
		model.addAttribute("nudge", this.obj);
		// survey.html은 질문 세트를 표시하는 템플릿 파일입니다.
	    return "survey";
	}
	

	// 세번째 [제출 시]
	@PostMapping("/survey/{page}")
	public String surveyPost(@PathVariable("page") int page, @RequestParam Map<String, String> answers,
						  HttpSession session, Model model) throws URISyntaxException, ParseException {
		
		// 제출 시간을 측정하고 시간 차이 값 구하기
		etime = Instant.now();
		int elapsedTime= (int) (Duration.between(stime, etime).toMillis() / 1000);
		System.out.println("소요시간:"+elapsedTime+"ms");
		
		int i = 0;
        int j = 0;
        
        if (page == 2) {
        	i = 1;
        	j = 6;
        	this.answerService.update1(this.sessionId, answers);
        } else if (page == 3) {
        	i = 7;
        	j = 15;
        	this.answerService.update2(this.sessionId, answers);
        } else {
        	i = 16;
        	j = 21;
        	// 아래에서 update3를 사용합니다. (결과 메세지를 넣기 위해)
        }
        
        // parameter setting
        String apiUrl = "http://192.168.0.50:8000/create/answer";
        URI uri = new URI(apiUrl);
        
        HashMap<String, Integer> body = new HashMap<>();
        body.put("userId", this.sessionId);
		
        for (int k = i; k <= j; k++) {
        	String answerString = answers.get("answers[" + k + "]");
    		body.put("Q" + k, Integer.parseInt(answerString));
    	}
	        
		body.put("elapsedTime", elapsedTime);
		
		
		
        // body.put("uid", uid); 등 정보를 계속 보낼 수 있음
        HttpEntity<HashMap<String, Integer>> entity = new HttpEntity<>(body, new HttpHeaders());

        // 정보를 주고 받음
        RestTemplate rest = new RestTemplate();
        ResponseEntity<String> response = rest.exchange(uri, HttpMethod.POST, entity, String.class);
	    JSONParser json = new JSONParser();
	    JSONObject obj = (JSONObject) json.parse(response.getBody());
//	    model.addAttribute("obj", obj.get("Answer").toString());
	    System.out.println(obj.get("Answer").toString());
	    this.obj = obj.get("Answer").toString();
	    
	    
	    
		
	    if (page == 4) {
			System.out.println(obj.get("evaluation").toString());
	    	this.answerService.update3(this.sessionId, answers, this.obj, obj.get("evaluation").toString());

			// 제출 수 카운트 -> 어드민 페이지에서 사용
			Optional<AdminPage> adminPageSubmit = this.adminpageRepository.findByDate(this.TODAY);
			if (adminPageSubmit.isPresent()) {
				AdminPage adminPage1 = adminPageSubmit.get();
				adminPage1.setSubmitCount(adminPage1.getSubmitCount() + 1);
				this.adminpageRepository.save(adminPage1);
			} else { // else 구문은 서버 환경에서는 필요 없음
				this.adminPageService.create(this.TODAY);
			}

	    	return "redirect:/result";
	    } else {
	    	return "redirect:/survey/{page}";
	    }
	}
	
	// 네번째 [결과화면]
	@GetMapping("/result")
	public String result(Model model) {
//		결과 화면에서 표출 안하기로 결정
//		model.addAttribute("obj", this.obj);
		return "result";
	}

	@GetMapping("/")
	public String svelte() {
		return "index";
	}
}
