package prototype.example.demo.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class CsrfTokenFilter extends OncePerRequestFilter {

    public static final String CSRF_TOKEN_NAME = "csrf_token";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String sessionToken = (session != null) ? (String) session.getAttribute(CSRF_TOKEN_NAME) : null;
        String requestToken = request.getParameter(CSRF_TOKEN_NAME);

        // POST 요청일 경우 토큰 검증
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            if (sessionToken == null || requestToken == null || !sessionToken.equals(requestToken)) {
                // 토큰이 없거나 일치하지 않으면 Forbidden 에러 발생
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF Token");
                return;
            }
        }

        // 세션에 토큰이 없으면 새로 생성하여 저장
        if (sessionToken == null) {
            String newToken = UUID.randomUUID().toString();
            if (session != null) {
                session.setAttribute(CSRF_TOKEN_NAME, newToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}