package com.aisa.survey.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Question {
	
	@Id
	private Integer questionId;
	
	private String level_1;
	
	private String level_2;

	
	private String question;
	
	private LocalDateTime createDate;
}
