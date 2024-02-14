package com.aisa.survey.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.aisa.survey.entity.Answer;
import com.aisa.survey.repository.AnswerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerService {
	
	private final AnswerRepository answerRepository;
	
	// 성별, 나이 입력 후 DB 생성, 해당 id 리턴
	public int create(String gender, String age) {
		Answer answer = new Answer();
		answer.setGender(Integer.parseInt(gender));
		answer.setAge(Integer.parseInt(age));
		answer.setCreateDate(LocalDateTime.now());
		int id = this.answerRepository.save(answer).getId();
		return id;
	}

	public void update1(int sessionId, Map<String, String> answers) {
	    Optional<Answer> optionalAnswer = answerRepository.findById(sessionId);
	    if ( optionalAnswer.isPresent()) {
	    	Answer answer = optionalAnswer.get();
	    	answer.setA1(Integer.parseInt(answers.get("answers[" + 1 + "]")));
	    	answer.setA2(Integer.parseInt(answers.get("answers[" + 2 + "]")));
	    	answer.setA3(Integer.parseInt(answers.get("answers[" + 3 + "]")));
	    	answer.setA4(Integer.parseInt(answers.get("answers[" + 4 + "]")));
	    	answer.setA5(Integer.parseInt(answers.get("answers[" + 5 + "]")));
	    	answer.setA6(Integer.parseInt(answers.get("answers[" + 6 + "]")));
	    	this.answerRepository.save(answer);
	    }
	}
	
	public void update2(int sessionId, Map<String, String> answers) {
	    Optional<Answer> optionalAnswer = answerRepository.findById(sessionId);
	    if ( optionalAnswer.isPresent()) {
	    	Answer answer = optionalAnswer.get();
	    	answer.setA1(Integer.parseInt(answers.get("answers[" + 7 + "]")));
	    	answer.setA2(Integer.parseInt(answers.get("answers[" + 8 + "]")));
	    	answer.setA3(Integer.parseInt(answers.get("answers[" + 9 + "]")));
	    	answer.setA4(Integer.parseInt(answers.get("answers[" + 10 + "]")));
	    	answer.setA5(Integer.parseInt(answers.get("answers[" + 11 + "]")));
	    	answer.setA6(Integer.parseInt(answers.get("answers[" + 12 + "]")));
	    	answer.setA6(Integer.parseInt(answers.get("answers[" + 13 + "]")));
	    	answer.setA6(Integer.parseInt(answers.get("answers[" + 14 + "]")));
	    	answer.setA6(Integer.parseInt(answers.get("answers[" + 15 + "]")));
	    	this.answerRepository.save(answer);
	    }
	}
	
	public void update3(int sessionId, Map<String, String> answers) {
	    Optional<Answer> optionalAnswer = answerRepository.findById(sessionId);
	    if ( optionalAnswer.isPresent()) {
	    	Answer answer = optionalAnswer.get();
	    	answer.setA1(Integer.parseInt(answers.get("answers[" + 16 + "]")));
	    	answer.setA2(Integer.parseInt(answers.get("answers[" + 17 + "]")));
	    	answer.setA3(Integer.parseInt(answers.get("answers[" + 18 + "]")));
	    	answer.setA4(Integer.parseInt(answers.get("answers[" + 19 + "]")));
	    	answer.setA5(Integer.parseInt(answers.get("answers[" + 20 + "]")));
	    	answer.setA6(Integer.parseInt(answers.get("answers[" + 21 + "]")));
	    	this.answerRepository.save(answer);
	    }
	}

}
