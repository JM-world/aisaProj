package com.aisa.survey;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.aisa.survey.entity.Answer;
import com.aisa.survey.repository.AnswerRepository;


@SpringBootTest
public class AsiaApplicationTests {

	@Autowired
	private AnswerRepository answerRepository;
	
	
	@Test
	void test() {
		Answer a1 = new Answer();
		a1.setGender(1);
		a1.setAge(30);
		a1.setCreateDate(LocalDateTime.now());
		a1.setA14(4);
		a1.setA15(3);
		a1.setA16(2);
		a1.setA17(1);
		a1.setA18(2);
		a1.setA19(3);
		a1.setA20(4);
		this.answerRepository.save(a1);
	}

}
