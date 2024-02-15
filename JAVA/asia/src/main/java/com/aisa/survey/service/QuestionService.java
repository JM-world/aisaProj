package com.aisa.survey.service;

import org.springframework.stereotype.Service;

import com.aisa.survey.entity.Question;
import com.aisa.survey.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {
	
	private final QuestionRepository questionRepository;
	
	
	

}
