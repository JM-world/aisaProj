package com.aisa.survey;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class p {

    public static void main(String[] args) {
        LocalDate date = LocalDate.now();
        String startDate = date.with(DayOfWeek.MONDAY).minusWeeks(1).format(DateTimeFormatter.ofPattern("yy.MM.dd"));
        String endDate = date.with(DayOfWeek.SUNDAY).minusWeeks(1).format(DateTimeFormatter.ofPattern("yy.MM.dd"));

        System.out.println(startDate + " " + endDate);
    }
}
