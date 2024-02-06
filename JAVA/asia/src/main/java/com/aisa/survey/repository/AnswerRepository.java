package com.aisa.survey.repository;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aisa.survey.entity.Answer;

@Configuration
@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer>{

}
