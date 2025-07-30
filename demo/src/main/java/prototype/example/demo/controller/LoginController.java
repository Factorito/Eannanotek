package prototype.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import prototype.example.demo.entity.User;
import prototype.example.demo.repository.UserRepository;

import java.util.Optional;


@Controller
@RequestMapping(value = "/prototype")
public class LoginController{

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Optional<User> userOpt = userRepository.findByEmailAndPassword(email, password);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            session.setAttribute("user", user);
            if(user.isAA()){
                session.setAttribute("isAdmin", true); // 세션에 관리자임을 표시
                return "redirect:/prototype/main";
            }else{
                session.setAttribute("isAdmin", false); // 세션에 일반 사용자임을 표시
                return "redirect:/prototype/main";
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "이메일이나 비밀번호가 일치하지 않습니다.");
            return "redirect:/prototype/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/prototype/login";
    }


    @GetMapping("/login")
public String loginPage() {
    return "CQ_LOGIN_001";
}

}
