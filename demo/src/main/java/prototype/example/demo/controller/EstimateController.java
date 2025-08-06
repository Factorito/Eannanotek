package prototype.example.demo.controller;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import prototype.example.demo.entity.Ci;
import prototype.example.demo.entity.Formula;
import prototype.example.demo.entity.Module;
import prototype.example.demo.repository.CiRepository;
import prototype.example.demo.repository.FormulaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import prototype.example.demo.repository.ModuleRepository;
import prototype.example.demo.service.Formservice;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;


//CQ_QEQ_001.html에서 견적서 계산 신호 매핑
@Controller
@RequestMapping(value = "/prototype")
public class EstimateController {

    @Autowired
    private FormulaRepository formulaRepository;

    @Autowired
    private CiRepository ciRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private Formservice formService;


    @PostMapping("/submit")
    public String submit(
            @RequestParam String email,
            @RequestParam int width,
            @RequestParam int height,
            @RequestParam String led,
            @RequestParam String formulaName,
            @RequestParam LocalDate install_day,
            @RequestParam String location,
            @RequestParam String CaseType,
            @RequestParam String env,
            @RequestParam String field,

            RedirectAttributes redirectAttributes
    ) {
        // 계산 및 저장
        Map<String, Object> result = formService.processEstimate(
                email, width, height, led, formulaName,
                install_day, location, CaseType, env, field
        );

        // Ci 엔티티의 email2, installDay 필드와 일치하도록 addFlashAttribute
        redirectAttributes.addFlashAttribute("email2", email);
        redirectAttributes.addFlashAttribute("installDay", install_day);

        // Formservice에서 계산된 모든 결과값을 'estimate'라는 이름으로 모델에 담아 redirect
        redirectAttributes.addFlashAttribute("estimate", result);

        // CQ_QEQ_002.html에서 직접 참조하는 price, vat, total 변수를 위해 개별적으로도 전달
        // (Formservice의 result 맵에 이미 포맷팅된 문자열로 들어있음)
        redirectAttributes.addFlashAttribute("price", result.getOrDefault("price", "0원"));
        redirectAttributes.addFlashAttribute("vat", result.getOrDefault("vat", "0원"));
        redirectAttributes.addFlashAttribute("total", result.getOrDefault("total", "0원"));

        return "redirect:/prototype/qeq2";
    }

}
