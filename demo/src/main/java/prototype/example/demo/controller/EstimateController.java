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


        redirectAttributes.addFlashAttribute("email2", email); // Ci 엔티티의 email2 필드명과 일치
        redirectAttributes.addFlashAttribute("installDay", install_day); // Ci 엔티티의 installDay 필드명과 일치
        // 결과값 model에 담아 redirect
        redirectAttributes.addFlashAttribute("estimate", result);
        return "redirect:/prototype/qeq2";
    }

}
