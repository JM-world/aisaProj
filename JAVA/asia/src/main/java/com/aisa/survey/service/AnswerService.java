package com.aisa.survey.service;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
        if (optionalAnswer.isPresent()) {
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
        if (optionalAnswer.isPresent()) {
            Answer answer = optionalAnswer.get();
            answer.setA7(Integer.parseInt(answers.get("answers[" + 7 + "]")));
            answer.setA8(Integer.parseInt(answers.get("answers[" + 8 + "]")));
            answer.setA9(Integer.parseInt(answers.get("answers[" + 9 + "]")));
            answer.setA10(Integer.parseInt(answers.get("answers[" + 10 + "]")));
            answer.setA11(Integer.parseInt(answers.get("answers[" + 11 + "]")));
            answer.setA12(Integer.parseInt(answers.get("answers[" + 12 + "]")));
            answer.setA13(Integer.parseInt(answers.get("answers[" + 13 + "]")));
            answer.setA14(Integer.parseInt(answers.get("answers[" + 14 + "]")));
            answer.setA15(Integer.parseInt(answers.get("answers[" + 15 + "]")));
            this.answerRepository.save(answer);
        }
    }

    public void update3(int sessionId, Map<String, String> answers, String obj) {
        Optional<Answer> optionalAnswer = answerRepository.findById(sessionId);
        if (optionalAnswer.isPresent()) {
            Answer answer = optionalAnswer.get();
            answer.setA16(Integer.parseInt(answers.get("answers[" + 16 + "]")));
            answer.setA17(Integer.parseInt(answers.get("answers[" + 17 + "]")));
            answer.setA18(Integer.parseInt(answers.get("answers[" + 18 + "]")));
            answer.setA19(Integer.parseInt(answers.get("answers[" + 19 + "]")));
            answer.setA20(Integer.parseInt(answers.get("answers[" + 20 + "]")));
            answer.setA21(Integer.parseInt(answers.get("answers[" + 21 + "]")));
            answer.setResultMessage(obj);
            this.answerRepository.save(answer);
        }
    }

    public Page<Answer> answerList(int page, int num) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, num, Sort.by(sorts));
        return this.answerRepository.findAll(pageable);
    }

    public List<Integer> genderRatio() {
        List<Integer> genderRatio = new ArrayList<>();
        genderRatio.add(this.answerRepository.countByGenderMale());
        genderRatio.add(this.answerRepository.countByGenderFemale());
        return genderRatio;
    }

    public List<Double> aiSurveyResultBefore() {
        List<Object[]> objects = this.answerRepository.aiSurveyResultBefore();
        List<Double> avgBefore = new ArrayList<>();

        if (!objects.isEmpty()) {
            Object[] result = objects.get(0);

            for (int i=0; i < 21; i++) {
                avgBefore.add((Double) result[i]);
            }
        }

        return avgBefore;
    }

    public List<Double> aiSurveyResultAfter() {
        List<Object[]> objects = this.answerRepository.aiSurveyResultAfter();
        List<Double> avgAfter = new ArrayList<>();

        if(!objects.isEmpty()) {
            Object[] result = objects.get(0);

            for (int i=0; i < 21; i++) {
                avgAfter.add((Double) result[i]);
            }
        }

        return avgAfter;
    }

    public List<Double> ageAvg() {
        List<Double> ageAvg = new ArrayList<>();
        List<Object[]> middleSchool = this.answerRepository.middleSchoolAvg();
        List<Object[]> highSchool = this.answerRepository.highSchoolAvg();
        List<Object[]> notSchool = this.answerRepository.notSchoolAvg();

        if (!middleSchool.isEmpty()) {
            Object[] middleOb = middleSchool.get(0);

            // 비판적 사고
            Double middle1 = 0.0;
            // 의사소통
            Double middle2 = 0.0;
            // 창의력
            Double middle3 = 0.0;

            for(int i = 0; i < 21; i++) {
                if (i<6) {
                    middle1 += (Double) middleOb[i];
                } else if (i<15) {
                    middle2 += (Double) middleOb[i];
                } else {
                    middle3 += (Double) middleOb[i];
                }
            }
            middle1 = middle1 / 6.0;
            middle2 = middle2 / 9.0;
            middle3 = middle3 / 6.0;
            ageAvg.add(Math.round(middle1 * 10) / 10.0);
            ageAvg.add(Math.round(middle2 * 10) / 10.0);
            ageAvg.add(Math.round(middle3 * 10) / 10.0);
        }

        if (!notSchool.isEmpty()) {
            Object[] highOb = notSchool.get(0);

            Double high1 = 0.0;
            Double high2 = 0.0;
            Double high3 = 0.0;

            for(int i = 0; i < 21; i++) {
                if (i<6) {
                    high1 += (Double) highOb[i];
                } else if (i<15) {
                    high2 += (Double) highOb[i];
                } else {
                    high3 += (Double) highOb[i];
                }
            }
            high1 = high1 / 6.0;
            high2 = high2 / 9.0;
            high3 = high3 / 6.0;
            ageAvg.add(Math.round(high1 * 10) / 10.0);
            ageAvg.add(Math.round(high2 * 10) / 10.0);
            ageAvg.add(Math.round(high3 * 10) / 10.0);
        }

        if (!notSchool.isEmpty()) {
            Object[] notOb = notSchool.get(0);

            Double not1 = 0.0;
            Double not2 = 0.0;
            Double not3 = 0.0;

            for(int i = 0; i < 21; i++) {
                if (i<6) {
                    not1 += (Double) notOb[i];
                } else if (i<15) {
                    not2 += (Double) notOb[i];
                } else {
                    not3 += (Double) notOb[i];
                }
            }
            not1 = not1 / 6.0;
            not2 = not2 / 9.0;
            not3 = not3 / 6.0;
            ageAvg.add(Math.round(not1 * 10) / 10.0);
            ageAvg.add(Math.round(not2 * 10) / 10.0);
            ageAvg.add(Math.round(not3 * 10) / 10.0);
        }

        return ageAvg;
    }

    public List<Integer> age() {
        List<Integer> age = new ArrayList<>();
        age.add(this.answerRepository.middleSchool());
        age.add(this.answerRepository.highSchool());
        age.add(this.answerRepository.notSchool());
        return age;
    }

    public Page<Answer> filterAnswerAll(int page, int num, String gender, String age, String startDate, String endDate, String category, String search) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, num, Sort.by(sorts));

        Specification<Answer> spec = AnswerRepository.FilterAnswerAll.filterByCriteria(gender, age, startDate, endDate, category);

        return answerRepository.findAll(spec, pageable);

    }

}
