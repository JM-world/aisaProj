package com.aisa.survey.service;

import com.aisa.survey.entity.AdminPage;
import com.aisa.survey.repository.AdminPageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminPageService  {

    private final AdminPageRepository adminPageRepository;

    public AdminPage create(String today) {
        AdminPage adminPage = new AdminPage();
        adminPage.setDate(today);
        adminPage.setVisitCount(1);
        adminPage.setSubmitCount(0);
        this.adminPageRepository.save(adminPage);

        return adminPage;
    }


    public List<Integer> sumOfVisitAndSubmit(String period) {
        LocalDate date = LocalDate.now();
        String endDate = "";
        String endDate2 = "";
        String startDate = "";
        String startDate2 = "";
        if (period.equals("Today") || period.equals("main")) {
            // 오늘 (규칙성을 위해 변수명 통일)
            endDate = date.format(DateTimeFormatter.ofPattern("yy.MM.dd"));

            // 어제
            startDate = date.minusDays(1).format(DateTimeFormatter.ofPattern("yy.MM.dd"));

        } else if (period.equals("Week")) {
            // 이번 주 (월요일부터 오늘까지)
            endDate = date.with(DayOfWeek.SUNDAY).format(DateTimeFormatter.ofPattern("yy.MM.dd"));
            startDate = date.with(DayOfWeek.MONDAY).format(DateTimeFormatter.ofPattern("yy.MM.dd"));
            // 오늘 날짜가 일요일이 아니라면
            if ( !date.format(DateTimeFormatter.ofPattern("yy.MM.dd")).equals(endDate) ) {
                endDate = date.format(DateTimeFormatter.ofPattern("yy.MM.dd"));
            }
            // 저번 주 (월~일)
            endDate2 = date.with(DayOfWeek.SUNDAY).minusWeeks(1).format(DateTimeFormatter.ofPattern("yy.MM.dd"));
            startDate2 = date.with(DayOfWeek.MONDAY).minusWeeks(1).format(DateTimeFormatter.ofPattern("yy.MM.dd"));

        } else {
            // 이번 달 (1일부터 현재까지)
            endDate = date.with(TemporalAdjusters.lastDayOfMonth()).format(DateTimeFormatter.ofPattern("yy.MM.dd"));
            startDate = date.withDayOfMonth(1).format(DateTimeFormatter.ofPattern("yy.MM.dd"));

            if ( !date.format(DateTimeFormatter.ofPattern("yy.MM.dd")).equals(endDate) ) {
                endDate = date.format(DateTimeFormatter.ofPattern("yy.MM.dd"));
            }

            // 저번 달
            endDate2 = date.with(TemporalAdjusters.lastDayOfMonth()).minusMonths(1).format(DateTimeFormatter.ofPattern("yy.MM.dd"));
            startDate2 = date.withDayOfMonth(1).minusMonths(1).format(DateTimeFormatter.ofPattern("yy.MM.dd"));
        }



        // 1,2번째 값은 방문자 수 || 3,4번째 값은 제출 수
        List<Integer> sumList = new ArrayList<>();

        if (period.equals("Today") || period.equals("main")) {
            Optional<AdminPage> today = this.adminPageRepository.findByDate(endDate);
            Optional<AdminPage> yesterday = this.adminPageRepository.findByDate(startDate);
            if (today.isEmpty()) {
                create(endDate);
                today = this.adminPageRepository.findByDate(endDate);
            } else if (yesterday.isEmpty()) {
                create(startDate);
                yesterday = this.adminPageRepository.findByDate(startDate);
            }
            if (today.isPresent() && yesterday.isPresent()) {
                sumList.add(today.get().getVisitCount());
                sumList.add(yesterday.get().getVisitCount());
                sumList.add(today.get().getSubmitCount());
                sumList.add(yesterday.get().getSubmitCount());
            }

        } else {
            List<AdminPage> entities = this.adminPageRepository.findAllByDateBetween(startDate, endDate);
            List<AdminPage> entities2 = this.adminPageRepository.findAllByDateBetween(startDate2, endDate2);

            // 방문자 수
            int sum1 = entities.stream()
                    .mapToInt(AdminPage::getVisitCount)
                    .sum();

            int sum2 = entities2.stream()
                    .mapToInt(AdminPage::getVisitCount)
                    .sum();

            // 제출 수
            int sum3 = entities.stream()
                    .mapToInt(AdminPage::getSubmitCount)
                    .sum();

            int sum4 = entities2.stream()
                    .mapToInt(AdminPage::getSubmitCount)
                    .sum();

            sumList.add(sum1);
            sumList.add(sum2);
            sumList.add(sum3);
            sumList.add(sum4);
        }

        return sumList;
    }

    public List<Integer> visitList(String period) {
        LocalDate date = LocalDate.now();
        List<Integer> result = new ArrayList<>();
        String startDate = "";
        String endDate = "";
        if (period.equals("main") || period.equals("Today")) {

            for (int i = 0; i <= 6; i++) {
                String aDay = date.minusDays(i).format(DateTimeFormatter.ofPattern("yy.MM.dd"));
                Optional<AdminPage> byDate = this.adminPageRepository.findByDate(aDay);
                if (byDate.isPresent()) {
                    result.add(byDate.get().getVisitCount());
                } else {
                    result.add(0);
                }
            }

        } else if (period.equals("Week")) {

            for (int i = 0; i <= 6; i++) {
                startDate = date.with(DayOfWeek.MONDAY).minusWeeks(i).format(DateTimeFormatter.ofPattern("yy.MM.dd"));
                endDate = date.with(DayOfWeek.SUNDAY).minusWeeks(i).format(DateTimeFormatter.ofPattern("yy.MM.dd"));
                if ( i == 0 ) {
                    endDate = date.format(DateTimeFormatter.ofPattern("yy.MM.dd"));
                }
                List<AdminPage> entities = this.adminPageRepository.findAllByDateBetween(startDate, endDate);
                int visitSum = entities.stream()
                        .mapToInt(AdminPage :: getVisitCount)
                        .sum();
                result.add(visitSum);
            }
        } else {

            for (int i = 0; i <= 6; i++) {
                startDate = date.withDayOfMonth(1).minusMonths(i).format(DateTimeFormatter.ofPattern("yy.MM.dd"));
                endDate = date.with(TemporalAdjusters.lastDayOfMonth()).minusMonths(i).format(DateTimeFormatter.ofPattern("yy.MM.dd"));

                if ( i == 0 ) {
                    endDate = date.format(DateTimeFormatter.ofPattern("yy.MM.dd"));
                }

                List<AdminPage> entities = this.adminPageRepository.findAllByDateBetween(startDate, endDate);
                int visitSum = entities.stream()
                        .mapToInt(AdminPage::getVisitCount)
                        .sum();
                result.add(visitSum);
            }
        }
        return result;
    }

    public List<Integer> submitList(String period) {
        LocalDate date = LocalDate.now();
        List<Integer> result = new ArrayList<>();
        String startDate = "";
        String endDate = "";
        if (period.equals("main") || period.equals("Today")) {

            for (int i = 0; i <= 6; i++) {
                String aDay = date.minusDays(i).format(DateTimeFormatter.ofPattern("yy.MM.dd"));
                Optional<AdminPage> byDate = this.adminPageRepository.findByDate(aDay);
                if (byDate.isPresent()) {
                    result.add(byDate.get().getSubmitCount());
                } else {
                    result.add(0);
                }
            }
        } else if (period.equals("Week")) {

            for (int i = 0; i <= 6; i++) {
                startDate = date.with(DayOfWeek.MONDAY).minusWeeks(i).format(DateTimeFormatter.ofPattern("yy.MM.dd"));
                endDate = date.with(DayOfWeek.SUNDAY).minusWeeks(i).format(DateTimeFormatter.ofPattern("yy.MM.dd"));
                if ( i == 0 ) {
                    endDate = date.format(DateTimeFormatter.ofPattern("yy.MM.dd"));
                }
                List<AdminPage> entities = this.adminPageRepository.findAllByDateBetween(startDate, endDate);
                int submitSum = entities.stream()
                        .mapToInt(AdminPage :: getSubmitCount)
                        .sum();
                result.add(submitSum);
            }
        } else {

            for (int i = 0; i <= 6; i++) {
                endDate = date.minusMonths(i).format(DateTimeFormatter.ofPattern("yy.MM.dd"));
                startDate = date.minusMonths(i + 1).format(DateTimeFormatter.ofPattern("yy.MM.dd"));

                if ( i == 0 ) {
                    endDate = date.format(DateTimeFormatter.ofPattern("yy.MM.dd"));
                }

                List<AdminPage> entities = this.adminPageRepository.findAllByDateBetween(startDate, endDate);
                int submitSum = entities.stream()
                        .mapToInt(AdminPage::getSubmitCount)
                        .sum();
                result.add(submitSum);
            }
        }
        return result;
    }


}
