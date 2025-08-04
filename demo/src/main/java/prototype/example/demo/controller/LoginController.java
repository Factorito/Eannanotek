package prototype.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping; // 주석 유지
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import prototype.example.demo.entity.SiteUser;
import prototype.example.demo.repository.UserRepository;

import java.util.Optional;


@Controller
public class LoginController{

    // @Autowired
    // private UserRepository userRepository;


    @GetMapping("/prototype/login")
    public String loginPage() {
        return "CQ_LOGIN_001";
    }

}