package prototype.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import prototype.example.demo.repository.FormulaRepository;
import prototype.example.demo.entity.Formula;
import org.springframework.web.bind.annotation.GetMapping;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/prototype/qeq1")
public class EstimateController {
    @Autowired
    private FormulaRepository formulaRepository;

    @PostMapping("/submit")
    public String handleQeq1Submit(@RequestParam Map<String, String> params, Model model) {
        // 입력값 파싱
        int width = Integer.parseInt(params.get("width"));
        int height = Integer.parseInt(params.get("height"));
        int quantity = 1; // 예시, 실제로는 입력값에서 받아야 함

        Map<String, Double> variables = new HashMap<>();
        variables.put("width", (double) width);
        variables.put("height", (double) height);
        variables.put("quantity", (double) quantity);

        // formula 테이블에서 계산식 읽기
        String[] formulaNames = {"내경", "외경"};
        for (String fname : formulaNames) {
            Formula formula = formulaRepository.findByName(fname).orElse(null);
            if (formula != null) {
                String expr = formula.getExpression();
                Expression e = new ExpressionBuilder(expr)
                        .variables(variables.keySet())
                        .build();
                for (String var : variables.keySet()) {
                    e.setVariable(var, variables.get(var));
                }
                double result = e.evaluate();
                model.addAttribute(fname, result);
            }
        }
        // 기타 입력값도 결과에 전달
        model.addAttribute("width", width);
        model.addAttribute("height", height);
        return "CQ_QEQ_002";
    }

    @GetMapping("/test")
    public String testForm() {
        return "CQ_QEQ_001";
    }
} 