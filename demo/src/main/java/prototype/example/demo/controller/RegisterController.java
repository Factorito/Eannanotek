package prototype.example.demo.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import prototype.example.demo.Dto.AddUserRequest;
import prototype.example.demo.service.Userservice;

import java.security.Principal;

@Controller
@RequestMapping("/prototype")
public class RegisterController {

    @Autowired
    private Userservice userservice;

    @GetMapping("/register")
    public String registerPage() {
        return "CQ_LOGIN_002";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String password2,
            @RequestParam(required = false) String termsConsent,
            RedirectAttributes redirectAttributes,
            @Valid AddUserRequest addUserRequest,
            BindingResult bindingResult
    ) {
        System.out.println("--- 회원가입 요청 데이터 수신 ---");
        System.out.println("Email: " + addUserRequest.getEmail());
        System.out.println("Phone: " + addUserRequest.getPhone());
        System.out.println("Password: " + addUserRequest.getPassword());
        System.out.println("Confirm Password: " + password2);
        System.out.println("Terms Consent: " + termsConsent);
        System.out.println("Binding Result Errors: " + bindingResult.getAllErrors());
        System.out.println("--------------------------------");

        if(bindingResult.hasErrors()) {
            System.out.println("Binding Result 에러 발생");
            return "CQ_LOGIN_002";
        }

        if (!addUserRequest.getPassword().equals(password2)) {
            System.out.println("비밀번호 불일치");
            redirectAttributes.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "redirect:/prototype/register";
        }

        if (!"on".equals(termsConsent)) {
            System.out.println("이용약관 미동의");
            redirectAttributes.addAttribute("error", "이용약관에 동의해야 합니다.");
            return "redirect:/prototype/register";
        }

        try {
            userservice.create(addUserRequest.getEmail(), addUserRequest.getPassword(), addUserRequest.getPhone());
            redirectAttributes.addAttribute("success", "true");
            System.out.println("회원가입 성공");
            return "redirect:/prototype/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addAttribute("emailError", "true");
            System.out.println("이메일 중복 에러: " + e.getMessage());
            return "redirect:/prototype/register";
        } catch (Exception e) {
            System.err.println("회원가입 실패: " + e.getMessage());
            redirectAttributes.addAttribute("registerError", "true");
            System.out.println("알 수 없는 에러: " + e.getMessage());
            return "redirect:/prototype/register";
        }
    }

    @PostMapping("/update-profile")
    public String updateProfile(
            Principal principal,
            @RequestParam String phone,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String confirmPassword,
            RedirectAttributes redirectAttributes
    ) {
        if (principal == null) {
            return "redirect:/prototype/login";
        }

        String email = principal.getName();

        if (password != null && !password.isEmpty() && !password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "redirect:/prototype/main2";
        }

        try {
            userservice.updateUser(email, phone, password);
            redirectAttributes.addFlashAttribute("success", "성공적으로 업데이트되었습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/prototype/main2";
    }

    @PostMapping("/send-email-security-code")
    public String sendEmailSecurityCode(@RequestParam String email, RedirectAttributes redirectAttributes) {
        System.out.println("--- 보안 코드 전송 요청 ---");
        System.out.println("수신된 이메일: " + email);
        try {
            userservice.sendSecurityCode(email);
            redirectAttributes.addFlashAttribute("message", "보안 코드가 이메일로 전송되었습니다.");
            System.out.println("보안 코드 전송 성공");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            System.out.println("보안 코드 전송 실패: " + e.getMessage());
        }
        return "redirect:/prototype/login3";
    }
}
