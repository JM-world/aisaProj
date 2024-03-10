package com.aisa.survey;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        n = n / 4;
        String result = "";

        for(int i = 0; i < n; i ++) {
            result += "Long ";
        }
        result += "int";

        System.out.println(result);
    }
}
