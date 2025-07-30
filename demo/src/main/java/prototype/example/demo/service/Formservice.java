package prototype.example.demo.service;

import net.objecthunter.exp4j.ExpressionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import prototype.example.demo.entity.Ci;
import prototype.example.demo.entity.Formula;
import prototype.example.demo.entity.Module;
import prototype.example.demo.repository.CiRepository;
import prototype.example.demo.repository.FormulaRepository;
import prototype.example.demo.repository.ModuleRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class Formservice {

    @Autowired private CiRepository ciRepository;
    @Autowired private FormulaRepository formulaRepository;
    @Autowired private ModuleRepository moduleRepository;

    public Map<String, Object> processEstimate(
            String email, int width, int height, String led, String formulaName,
            LocalDate installDay, String location, String CaseType, String env, String field
    ) {
        System.out.println("led = '" + led + "'");

        // 1. 모듈 정보 조회
        Module module = moduleRepository.findByName(led.trim())
                .orElseThrow(() -> new RuntimeException("모듈 정보 없음: " + led));

        // 2. 기본 계산 값 도출
        int cols = width / module.getWidth();
        int rows = height / module.getHeight();
        int quantity = cols * rows; // 총 모듈 개수
        int resX = cols * module.getPxWidth();
        int resY = rows * module.getPxHeight();
        int moduleUnitPrice = module.getUnitPrice(); // 모듈 1개당 단가

        // 3. 설치 비용 조회 (Formula 테이블에서 가져옴)
        Formula installCostFormula = formulaRepository.findByName("INSTALL_COST") // "INSTALL_COST"라는 이름으로 저장된 수식/값
                .orElseThrow(() -> new RuntimeException("설치 비용 정보 없음: INSTALL_COST"));
        double installCost = Double.parseDouble(installCostFormula.getExpression()); // expression에 숫자가 문자열로 저장되어 있으므로 파싱

        // 4. 총 예상 견적 수식 조회 (Formula 테이블에서 가져옴)
        // formulaName은 이 메서드의 파라미터로 받았으므로, 그대로 사용
        formulaName = "TOTAL_ESTIMATE_FORMULA";
        String finalFormulaName = formulaName;
        Formula totalEstimateFormula = formulaRepository.findByName(formulaName) // 예: "TOTAL_ESTIMATE_FORMULA"
                .orElseThrow(() -> new RuntimeException("총 예상 견적 수식 없음: " + finalFormulaName));

        // 5. 수식 계산
        // exp4j ExpressionBuilder에 필요한 모든 변수를 전달
        double estimatedPrice = new ExpressionBuilder(totalEstimateFormula.getExpression())
                .variables("module_unit_price", "quantity", "install_cost") // 사용할 변수들 정의
                .build()
                .setVariable("module_unit_price", moduleUnitPrice) // 모듈 1개 단가
                .setVariable("quantity", quantity) // 총 모듈 개수
                .setVariable("install_cost", installCost) // 설치 비용
                .evaluate();

        // 6. Ci 엔티티 저장 (기존 로직 유지)
        Ci ci = new Ci();
        ci.setEmail2(email);
        ci.setInstallDay(installDay);
        ci.setInstallReigon(location);
        ci.setCaseType(CaseType);
        ci.setModule(led);
        ci.setInstallEnviroment(env);
        ci.setField(field);
        ci.setWidth(width);
        ci.setHeight(height);
        ci.setDisplaySize(width + "x" + height);
        ci.setActualWidth((float) (width + 10));
        ci.setActualHeight((float) (height + 10));
        ci.setQuantity(quantity);
        ci.setAspectRatio("16:9"); // 이 부분도 나중에 Formula나 Module에서 가져올 수 있음
        ci.setPixelWidth(resX);
        ci.setPixelHeight(resY);
        ci.setEstimatedPrice((int) estimatedPrice);

        ciRepository.save(ci);

        // 7. 결과 Map 생성 및 반환 (기존 로직 유지)
        Map<String, Object> result = new HashMap<>();
        result.put("width", width);
        result.put("height", height);
        result.put("내경", width + "x" + height);
        result.put("외경", (width + 10) + "x" + (height + 10));
        result.put("모듈개수", quantity); // 추가
        result.put("해상도", resX + "x" + resY); // 추가
        result.put("모듈단가", moduleUnitPrice); // 추가
        result.put("설치비용", (int) installCost); // 추가
        result.put("공사금액", (int) estimatedPrice);
        result.put("부가세", estimatedPrice * 0.1);
        result.put("총합계", estimatedPrice * 1.1);
        return result;
    }
}