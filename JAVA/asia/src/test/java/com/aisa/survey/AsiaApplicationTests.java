package com.aisa.survey;

import java.time.Duration;
import java.time.Instant;
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
		Instant stime = Instant.now();
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Instant etime = Instant.now();
		System.out.println("소요시간:"+Duration.between(stime, etime).toMillis()+"ms");
		Long i = Duration.between(stime, etime).toMillis() / 1000;
		Integer l = i.intValue();
		
	}

}
