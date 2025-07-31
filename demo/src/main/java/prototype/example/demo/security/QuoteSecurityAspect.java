package prototype.example.demo.security;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import prototype.example.demo.entity.Ci;
import prototype.example.demo.repository.CiRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect // 이 클래스가 AOP Aspect임을 선언
@Component // Spring Bean으로 등록하여 DI 가능하게 함
public class QuoteSecurityAspect {

    @Autowired
    private CiRepository ciRepository;

    @Before("@annotation(CheckQuoteOwner)") // CheckQuoteOwner 어노테이션이 붙은 모든 메서드 이전에 실행
    public void checkOwnership(JoinPoint joinPoint) {
        // 1. 현재 로그인한 사용자 정보 가져오기
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
        String currentUserEmail = (String) session.getAttribute("email"); // 세션에 저장된 이메일 키에 따라 변경 필요

        if (currentUserEmail == null) {
            throw new SecurityException("로그인이 필요합니다.");
        }

        // 2. 메서드의 인자에서 견적서 ID 가져오기
        String quoteEmail = null;
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof String) {
                quoteEmail = (String) arg;
                break;
            }
        }

        if (quoteEmail == null) {
            throw new IllegalArgumentException("견적서 ID를 찾을 수 없습니다.");
        }

        // 3. DB에서 견적서 정보 조회
        Ci quote = ciRepository.findById(quoteEmail).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 견적서입니다.")
        );

        // 4. 소유권 확인 로직 실행
        if (!currentUserEmail.equals(quote.getEmail2())) {
            throw new SecurityException("해당 견적서에 대한 접근 권한이 없습니다.");
        }
    }
}