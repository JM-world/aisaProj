package com.aisa.survey.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Answer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Integer gender;
	
	private Integer age;
	
	private LocalDateTime createDate;
	
	private Integer a1;
	
	private Integer a2;
	
	private Integer a3;
	
	private Integer a4;
	
	private Integer a5;
	
	private Integer a6;
	
	private Integer a7;
	
	private Integer a8;
	
	private Integer a9;
	
	private Integer a10;
	
	private Integer a11;
	
	private Integer a12;
	
	private Integer a13;
	
	private Integer a14;
	
	private Integer a15;
	
	private Integer a16;
	
	private Integer a17;
	
	private Integer a18;
	
	private Integer a19;
	
	private Integer a20;
	
}
