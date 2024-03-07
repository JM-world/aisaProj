package com.aisa.survey.controller;

import com.aisa.survey.entity.Admin;
import com.aisa.survey.entity.AdminPage;
import com.aisa.survey.entity.Answer;
import com.aisa.survey.repository.AdminPageRepository;
import com.aisa.survey.service.AdminPageService;
import com.aisa.survey.service.AnswerService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.aisa.survey.form.AdminCreateForm;
import com.aisa.survey.service.AdminService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    private final AdminPageService adminPageService;

    private final AnswerService answerService;

    @Value("${KEY.CODE}")
    private String KEY_CODE;

    @GetMapping("/login")
    public String login() {
        return "admin/login_form";
    }

    @GetMapping("/signup")
    public String signup(AdminCreateForm adminCreateForm) {
        return "admin/signup_form";
    }

    @PostMapping("signup")
    public String signup(@Valid AdminCreateForm adminCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/signup_form";
        }

        if (!adminCreateForm.getPassword1().equals(adminCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "패스워드가 서로 일치하지 않습니다.");
            return "/admin/signup_form";
        }


        if (!adminCreateForm
                .getKeyCode().equals(this.KEY_CODE)) {
            bindingResult.rejectValue("keyCode", "keyCodeInCorrect",
                    "잘못된 방식의 ADMIN KEY입니다.");
            return "/admin/signup_form";

        }

        System.out.println(adminCreateForm.getName() + " " + adminCreateForm.getPassword1());
        adminService.create(adminCreateForm.getName(), adminCreateForm.getPassword1());
        System.out.println(adminCreateForm.getName() + " 가입완료");
        return "redirect:/admin/login";
    }

    @GetMapping("/main/{buttonValue}")
    public String adminMain(@PathVariable("buttonValue") String buttonValue, Model model) {

        List<Integer> adminPages = this.adminPageService.sumOfVisitAndSubmit(buttonValue);
        List<Integer> visitList = this.adminPageService.visitList(buttonValue);
        List<Integer> submitList = this.adminPageService.submitList(buttonValue);

        Gson gson = new Gson();
        String visitStr = gson.toJson(visitList);
		String submitStr = gson.toJson(submitList);

		// 방문, 제출 숫자 표시할 데이터
        model.addAttribute("visit", adminPages.get(0));
        model.addAttribute("submit", adminPages.get(2));
        model.addAttribute("visitBefore", adminPages.get(1));
        model.addAttribute("submitBefore", adminPages.get(3));

		// 방문, 제출 Chart.js에 들어갈 데이터
        model.addAttribute("visitStr", visitStr);
		model.addAttribute("submitStr", submitStr);

        // 제출 현황
        model.addAttribute("answerList", this.answerService.answerList(0, 5));

        // 성별 분포
        model.addAttribute("genderRatio", this.answerService.genderRatio());

        // 연령 분포
        model.addAttribute("age", this.answerService.age());

        // ai 차트 데이터
            // 일반 설문
        model.addAttribute("avgBefore", this.answerService.aiSurveyResultBefore());
            // 성실 데이터
        model.addAttribute("avgAfter", this.answerService.aiSurveyResultAfter());

        // 연령별 비교
        model.addAttribute("ageAvg", this.answerService.ageAvg());





        if (!buttonValue.equals("main")) {
            return "/admin/admin_main :: #mainCard";

        }

        return "/admin/admin_main";
    }

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue = "0") int page,
                       @RequestParam(value = "gender", defaultValue = "성별") String gender,
                       @RequestParam(value = "age", defaultValue = "연령") String age,
                       @RequestParam(value = "startDate", defaultValue = "시작 날짜") String startDate,
                       @RequestParam(value = "endDate", defaultValue = "종료 날짜") String endDate,
                       @RequestParam(value = "category", defaultValue = "구분") String category,
                       @RequestParam(value = "search", defaultValue = "0") String search

                       ) {

        Page<Answer> answerList = this.answerService.filterAnswerAll(page, 7, gender, age, startDate, endDate, category, search);
        model.addAttribute("answerList", answerList);
        model.addAttribute("currentPage", answerList.getNumber());
        model.addAttribute("totalPages", answerList.getTotalPages());
        model.addAttribute("hasPrevious", answerList.hasPrevious());
        model.addAttribute("hasNext", answerList.hasNext());

        return search.equals("0") ? "/admin/admin_list" : "/admin/admin_list :: #result";
    }


    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") int id, Model model) {
        Answer member = this.answerService.findId(id);

        // 성실 판단 전체 유저 평균
        model.addAttribute("avgAfter", this.answerService.aiSurveyResultAfter());
        // 해당 유저 문항
        model.addAttribute("answerNums", this.answerService.answerNums(id));
        // 해당 유저 정보
        model.addAttribute("member", member);
        // 제출일 표시
        model.addAttribute("memberDate", member.getCreateDate().format(DateTimeFormatter.ofPattern("yy.MM.dd")));
        // evaluation 표시
        model.addAttribute("resultMessage", member.getResultMessage()
                .replaceAll("1\\. ['\"]?비판적 사고 능력['\"]?:?", "<br><br><b style='background-color: white; color: rgb(250,91,91); font-size:1.2em; font-weight: 900;'>1. 비판적 사고 능력</b><br>")
                .replaceAll("2\\. ['\"]?의사소통 능력['\"]?:?", "<br><br><b style='background-color: white; color: rgba(5,95,255); font-size:1.2em; font-weight: 900;'>2. 의사소통 능력</b><br>")
                .replaceAll("3\\. ['\"]?창의적 사고 능력['\"]?:?", "<br><br><b style='background-color: white; color: rgb(64,129,19); font-size:1.2em; font-weight: 900;'>3. 창의적 사고 능력</b><br>")
                .replaceAll("전체 요약:", "<br><br><b style='background-color: white; font-size:1.2em; font-weight: 900;'>전체 요약</b><br>"));

        return "/admin/admin_detail";
    }

}
