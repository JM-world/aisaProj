package com.aisa.survey.Service;

import java.time.LocalDateTime;
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

	public void update(int sessionId, String a1, String a2, String a3, String a4, String a5, String a6) {
	    Optional<Answer> optionalAnswer = answerRepository.findById(sessionId);
	    if ( optionalAnswer.isPresent()) {
	    	Answer answer = optionalAnswer.get();
	    	answer.setA1(Integer.parseInt(a1));
	    	answer.setA2(Integer.parseInt(a2));
	    	answer.setA3(Integer.parseInt(a3));
	    	answer.setA4(Integer.parseInt(a4));
	    	answer.setA5(Integer.parseInt(a5));
	    	answer.setA6(Integer.parseInt(a6));
	    	this.answerRepository.save(answer);
	    	
	    }
	    
		
	}

}
