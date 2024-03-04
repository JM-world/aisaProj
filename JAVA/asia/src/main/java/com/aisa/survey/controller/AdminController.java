package com.aisa.survey.controller;

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

    LocalDate localDate = LocalDate.now();

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
    public String list() {
        return "/admin/admin_list";
    }

    @GetMapping("/aaa")
    public String aaa() {
        return "/admin/aaa";
    }

}
