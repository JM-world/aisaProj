package com.aisa.survey.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aisa.survey.entity.Answer;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Configuration
@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer>, JpaSpecificationExecutor<Answer> {

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


    public class FilterAnswerAll {
        public static Specification<Answer> filterByCriteria(String gender, String age, String startDate, String endDate, String category) {
            return (Root<Answer> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
                List<Predicate> predicates = new ArrayList<>();

                // 성별 조건
                if (!gender.equals("성별")) {
                    predicates.add(cb.equal(root.get("gender"), gender.equals("남자") ? 1 : 2));
                }

                // 연령 조건
                if (!age.equals("연령")) {
                    if (age.equals("중학생")) {
                        predicates.add(cb.between(root.get("age"), 14, 16));
                    } else if (age.equals("고등학생")) {
                        predicates.add(cb.between(root.get("age"), 17, 19));
                    } else if (age.equals("기타")) {
                        predicates.add(cb.or(cb.greaterThanOrEqualTo(root.get("age"), 20), cb.lessThanOrEqualTo(root.get("age"), 13)));
                    }
                }

                // 날짜 조건


                // DateTimeFormatter를 사용하여 날짜 형식 지정
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");


                if (!"시작 날짜".equals(startDate) && !"종료 날짜".equals(endDate)) {
                    LocalDate start = LocalDate.parse(startDate, formatter);
                    LocalDate end = LocalDate.parse(endDate, formatter).plusDays(1); // endDate의 다음 날짜를 지정하여 포함시킴
                    ZonedDateTime startDateTime = start.atStartOfDay(ZoneId.systemDefault());
                    ZonedDateTime endDateTime = end.atStartOfDay(ZoneId.systemDefault());
                    Date startDateValue = Date.from(startDateTime.toInstant());
                    Date endDateValue = Date.from(endDateTime.toInstant());
                    predicates.add(cb.between(root.get("createDate"), startDateValue, endDateValue));
                } else if (!"시작 날짜".equals(startDate)) {
                    LocalDate start = LocalDate.parse(startDate, formatter);
                    ZonedDateTime startDateTime = start.atStartOfDay(ZoneId.systemDefault());
                    Date startDateValue = Date.from(startDateTime.toInstant());
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createDate"), startDateValue));
                } else if (!"종료 날짜".equals(endDate)) {
                    LocalDate end = LocalDate.parse(endDate, formatter).plusDays(1); // endDate의 다음 날짜를 지정하여 포함시킴
                    ZonedDateTime endDateTime = end.atStartOfDay(ZoneId.systemDefault());
                    Date endDateValue = Date.from(endDateTime.toInstant());
                    predicates.add(cb.lessThanOrEqualTo(root.get("createDate"), endDateValue));
                }

                // 카테고리 조건
                if (!category.equals("구분")) {
                    predicates.add(cb.equal(root.get("resultMessage"), category.equals("성실") ? "1" : "2"));
                }

                return cb.and(predicates.toArray(new Predicate[0]));
            };
        }
    }






}
