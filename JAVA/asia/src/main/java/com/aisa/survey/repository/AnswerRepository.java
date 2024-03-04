package com.aisa.survey.repository;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aisa.survey.entity.Answer;

import java.util.List;
import java.util.Objects;

@Configuration
@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer>{

    @Query("SELECT COUNT(a.gender) FROM Answer a WHERE a.gender = 1")
    int countByGenderMale();

    @Query("SELECT COUNT(a.gender) FROM Answer a WHERE a.gender = 2")
    int countByGenderFemale();

    @Query("SELECT AVG(a.a1), AVG(a.a2), AVG(a.a3), AVG(a.a4), AVG(a.a5), AVG(a.a6)" +
            ", AVG(a.a7), AVG(a.a8), AVG(a.a9), AVG(a.a10), AVG(a.a11), AVG(a.a12), AVG(a.a13), AVG(a.a14), AVG(a.a15)" +
            ", AVG(a.a16), AVG(a.a17), AVG(a.a18), AVG(a.a19), AVG(a.a20), AVG(a.a21) FROM Answer a")
    List<Object[]> aiSurveyResultBefore();

    @Query("SELECT AVG(a.a1), AVG(a.a2), AVG(a.a3), AVG(a.a4), AVG(a.a5), AVG(a.a6)" +
            ", AVG(a.a7), AVG(a.a8), AVG(a.a9), AVG(a.a10), AVG(a.a11), AVG(a.a12), AVG(a.a13), AVG(a.a14), AVG(a.a15)" +
            ", AVG(a.a16), AVG(a.a17), AVG(a.a18), AVG(a.a19), AVG(a.a20), AVG(a.a21) FROM Answer a WHERE a.resultMessage = '1'")
    List<Object[]> aiSurveyResultAfter();


    // 아래부터 주제별 연령 백분율
    @Query("SELECT AVG(a.a1), AVG(a.a2), AVG(a.a3), AVG(a.a4), AVG(a.a5), AVG(a.a6)" +
            ", AVG(a.a7), AVG(a.a8), AVG(a.a9), AVG(a.a10), AVG(a.a11), AVG(a.a12), AVG(a.a13), AVG(a.a14), AVG(a.a15)" +
            ", AVG(a.a16), AVG(a.a17), AVG(a.a18), AVG(a.a19), AVG(a.a20), AVG(a.a21) FROM Answer a where a.age >= 14 and a.age <=16 and a.resultMessage = '1'")
    List<Object[]> middleSchoolAvg();

    @Query("SELECT AVG(a.a1), AVG(a.a2), AVG(a.a3), AVG(a.a4), AVG(a.a5), AVG(a.a6)" +
            ", AVG(a.a7), AVG(a.a8), AVG(a.a9), AVG(a.a10), AVG(a.a11), AVG(a.a12), AVG(a.a13), AVG(a.a14), AVG(a.a15)" +
            ", AVG(a.a16), AVG(a.a17), AVG(a.a18), AVG(a.a19), AVG(a.a20), AVG(a.a21) FROM Answer a where a.age >= 17 and a.age <=19 and a.resultMessage = '1'")
    List<Object[]> highSchoolAvg();

    @Query("SELECT AVG(a.a1), AVG(a.a2), AVG(a.a3), AVG(a.a4), AVG(a.a5), AVG(a.a6)" +
            ", AVG(a.a7), AVG(a.a8), AVG(a.a9), AVG(a.a10), AVG(a.a11), AVG(a.a12), AVG(a.a13), AVG(a.a14), AVG(a.a15)" +
            ", AVG(a.a16), AVG(a.a17), AVG(a.a18), AVG(a.a19), AVG(a.a20), AVG(a.a21) FROM Answer a where a.age >= 20 or a.age <=13 and a.resultMessage = '1'")
    List<Object[]> notSchoolAvg();


    @Query("SELECT COUNT(a.age) from Answer a where a.age >= 14 and a.age <= 16")
    int middleSchool();

    @Query("SELECT COUNT(a.age) from Answer a where a.age >= 17 and a.age <= 19")
    int highSchool();

    @Query("SELECT COUNT(a.age) from Answer a where a.age >= 20 or a.age <= 13")
    int notSchool();





}
