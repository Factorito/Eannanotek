package prototype.example.demo.service;

import net.objecthunter.exp4j.ExpressionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import prototype.example.demo.entity.Ci;
import prototype.example.demo.entity.Formula;
import prototype.example.demo.entity.module;
import prototype.example.demo.repository.CiRepository;
import prototype.example.demo.repository.FormulaRepository;
import prototype.example.demo.repository.ModuleRepository;

import java.util.HashMap;
import java.util.Map;

@Service
public class Formservice {

    @Autowired private CiRepository ciRepository;
    @Autowired private FormulaRepository formulaRepository;
    @Autowired private ModuleRepository moduleRepository;

    public Map<String, Object> processEstimate(
            String email, int width, int height, String led, String formulaName,
            int installDay, String location, String caseType, String env, String industry
    ) {
        module module = moduleRepository.findByName(led)
                .orElseThrow(() -> new RuntimeException("모듈 정보 없음"));

        int cols = width / module.getWidth();
        int rows = height / module.getHeight();
        int quantity = cols * rows;
        int resX = cols * module.getPxWidth();
        int resY = rows * module.getPxHeight();
        int unitPrice = module.getUnitPrice();

        Formula formula = formulaRepository.findByName(formulaName)
                .orElseThrow(() -> new RuntimeException("수식 없음"));

        double estimatedPrice = new ExpressionBuilder(formula.getExpression())
                .variables("unit_price", "quantity")
                .build()
                .setVariable("unit_price", unitPrice)
                .setVariable("quantity", quantity)
                .evaluate();

        Ci ci = new Ci();
        ci.setEmail2(email);
        ci.setInstallDay(installDay);
        ci.setInstallReigon(location);
        ci.setCaseType(caseType);
        ci.setModule(led);
        ci.setInstallEnviroment(env);
        ci.setField(industry);
        ci.setWidth(width);
        ci.setHeight(height);
        ci.setDisplaySize(width + "x" + height);
        ci.setActualWidth((float) (width + 10));
        ci.setActualHeight((float) (height + 10));
        ci.setQuantity(quantity);
        ci.setAspectRatio("16:9");
        ci.setPixelWidth(resX);
        ci.setPixelHeight(resY);
        ci.setEstimatedPrice((int) estimatedPrice);

        ciRepository.save(ci);

        // 결과 map 생성 후 반환
        Map<String, Object> result = new HashMap<>();
        result.put("width", width);
        result.put("height", height);
        result.put("내경", width + "x" + height);
        result.put("외경", (width + 10) + "x" + (height + 10));
        result.put("공사금액", (int) estimatedPrice);
        result.put("부가세", estimatedPrice * 0.1);
        result.put("총합계", estimatedPrice * 1.1);
        return result;
    }
}
