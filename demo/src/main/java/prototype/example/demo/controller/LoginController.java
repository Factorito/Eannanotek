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
//@RequestMapping("/prototype")
public class LoginController{

    @Autowired
    private UserRepository userRepository;

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
