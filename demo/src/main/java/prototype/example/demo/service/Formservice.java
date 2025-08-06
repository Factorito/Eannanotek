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
import java.time.format.DateTimeFormatter; // 날짜 포맷 추가
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
        String totalFormulaKey = "TOTAL_ESTIMATE_FORMULA"; // 예시: 고정된 수식 이름
        Formula totalEstimateFormula = formulaRepository.findByName(totalFormulaKey)
                .orElseThrow(() -> new RuntimeException("총 예상 견적 수식 없음: " + totalFormulaKey));

        // 5. 수식 계산
        double estimatedPrice = new ExpressionBuilder(totalEstimateFormula.getExpression())
                .variables("module_unit_price", "quantity", "install_cost") // 사용할 변수들 정의
                .build()
                .setVariable("module_unit_price", moduleUnitPrice) // 모듈 1개 단가
                .setVariable("quantity", quantity) // 총 모듈 개수
                .setVariable("install_cost", installCost) // 설치 비용
                .evaluate();

        // 부가세 및 총합계 계산
        double vatAmount = estimatedPrice * 0.1;
        double finalTotalPrice = estimatedPrice + vatAmount;

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
        ci.setActualWidth((float) (width + 10)); // 예시 값, 실제 외경 계산 필요
        ci.setActualHeight((float) (height + 10)); // 예시 값, 실제 외경 계산 필요
        ci.setQuantity(quantity);
        ci.setAspectRatio("16:9"); // 이 부분도 나중에 Formula나 Module에서 가져올 수 있음
        ci.setPixelWidth(resX);
        ci.setPixelHeight(resY);
        ci.setEstimatedPrice((int) finalTotalPrice); // 최종 총합계를 저장하는 것이 일반적

        ciRepository.save(ci);

        // 7. 결과 Map 생성 및 반환
        Map<String, Object> result = new HashMap<>();

        // 견적 기본 정보
        result.put("requestDate", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        result.put("estimateNo", "E-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" + (ci.getEmail2() != null ? ci.getEmail2() : "001")); // Ci ID 활용
        result.put("companyName", "(주)이안하이텍"); // 고정 값
        result.put("estimateDate", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        result.put("companyNo", "135-86-03476"); // 고정 값
        result.put("recipientPlace", "··"); // 고정 값
        result.put("ceoName", "안치현"); // 고정 값
        result.put("recipient", "담당자귀하"); // 고정 값
        result.put("sender", "김규진 상무"); // 고정 값
        result.put("address", "충남 천안시 서북구 입장면 호당길63(입장)"); // 고정 값
        result.put("email", "kiki@eansolution.com"); // 고정 값
        result.put("phone", "041-415-1433"); // 고정 값

        // 견적 금액 (포맷팅하여 전달)
        result.put("price", formatCurrency(estimatedPrice)); // 공사금액
        result.put("vat", formatCurrency(vatAmount));       // 부가세
        result.put("total", formatCurrency(finalTotalPrice)); // 총 합계

        // 상세 규격 정보 (Module 엔티티의 필드를 활용)
        result.put("내경", width + "x" + height + " (mm)");
        result.put("외경", (width + 10) + "x" + (height + 10) + " (mm)"); // 예시, 실제 외경 계산 필요
        result.put("모듈개수", quantity);
        result.put("해상도", resX + "x" + resY + " Pixels");
        result.put("pixelPitch", "P" + module.getName() + "mm / " + module.getSmd()); // module.getName() 사용
        result.put("drivingMethod", "정전류 구동 / " + module.getFrequency() + "Hz"); // 'getDrivingMethod()' 없음, 임의 값
        result.put("moduleSize", module.getWidth() + "x" + module.getHeight() + "mm / " + module.getPxWidth() + "x" + module.getPxHeight()); // module의 width/height/pxWidth/pxHeight 사용
        result.put("brightness", module.getBrightness() + "cd/m² / 100,000시간"); // 'getScreenLifeTime()' 없음, 임의 값

        // powerConsumption이 null일 경우를 대비하여 기본값 0.0으로 처리
        double powerConsumptionValue = (module.getPowerConsumption() != null) ? module.getPowerConsumption().doubleValue() : 0.0;
        result.put("modulePower", String.format("%.2f", powerConsumptionValue) + "W/Module"); // powerConsumptionValue 직접 포맷
        result.put("powerMax", String.format("%.2f", (powerConsumptionValue * quantity * 1.5)) + "W"); // 'getPowerConsumptionMax()' 없음, 계산식 유지
        result.put("powerAvg", String.format("%.2f", (powerConsumptionValue * quantity * 0.8)) + "W"); // 'getPowerConsumptionAvg()' 없음, 계산식 유지
        result.put("etcSpec", "동기식 제어 시스템"); // 예시, 필요시 DB에서 가져오기

        // 항목별 단가 정보 (예시, 실제 데이터는 DB에서 가져와야 함)
        result.put("item1Unit", quantity + "EA"); // LED Screen 전광판 수량은 모듈 개수로 가정
        result.put("item1Price", formatCurrency(estimatedPrice)); // LED Screen 전광판 견적금액 (공사금액과 동일하게 가정)
        result.put("item2Unit", "1식"); // 철재베이스/제빙베이스/인입선
        result.put("item2Price", formatCurrency(500000)); // 예시 금액
        result.put("item5Unit", "1식"); // CMS/통합제어
        result.put("item5Price", formatCurrency(300000)); // 예시 금액
        result.put("item6Unit", "1식"); // 운용 PC Spec/Monitor
        result.put("item6Price", formatCurrency(800000)); // 예시 금액

        // 기타 조건
        result.put("paymentCondition", "계약시 60%, 완료시 40%");
        result.put("validPeriod", "견적일로부터 30일/발주 후 4주");
        result.put("ledChip", "A등급"); // 예시
        result.put("usage", "실내용 P" + module.getName() + "mm(" + CaseType + ")"); // module.getName() 사용

        // Spare Part 및 기타 설명
        result.put("sparePart", "LED 모듈 2개, 파워 서플라이 1개"); // 예시
        result.put("desc1", "• 최고급형 제어 특화 / 동기화 되는 네트워크 제어");
        result.put("desc2", "• Mobile / Wifi / USB / PC/ TABLET무선 리모컨제어 특화");
        result.put("desc3", "• 모든 자재는 KC 인증 마크");
        result.put("desc4", "• 무상 A/S 1년");

        return result;
    }

    /**
     * 숫자를 통화 형식 문자열로 포맷합니다. (예: 1234567 -> "1,234,567원")
     * @param amount 포맷할 숫자 (Long, Integer, Double 등 Number 타입)
     * @return 포맷된 통화 문자열
     */
    private String formatCurrency(Object amount) {
        if (amount == null) return "0원";
        long value;
        if (amount instanceof Number) {
            value = ((Number) amount).longValue();
        } else {
            try {
                value = Long.parseLong(String.valueOf(amount));
            } catch (NumberFormatException e) {
                return "0원"; // 숫자로 변환할 수 없으면 0원
            }
        }
        return String.format("%,d원", value);
    }
}
