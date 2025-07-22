package prototype.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String main() {
        return "redirect:/main";
    }

    @GetMapping("/main")
    public String mainPage() {
        return "redirect:/CQ_MAIN_1.html";
    }

    @GetMapping("/quotation1")
    public String quotation1() {
        return "redirect:/CQ_QEQ_1.html";
    }

    @GetMapping("/quotation2")
    public String quotation2() {
        return "redirect:/CQ_QEQ_02";
    }
} 