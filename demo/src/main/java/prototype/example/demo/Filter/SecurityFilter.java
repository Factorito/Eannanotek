package prototype.example.demo.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class SecurityFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.info("Request URI: " + request.getRequestURI());

        // XSS 방어를 위해 요청을 XssFilter 래퍼로 감싸서 필터 체인에 전달
        XssFilter wrappedRequest = new XssFilter(request);
        filterChain.doFilter(wrappedRequest, response);

        log.info("Response Status: " + response.getStatus());
    }
}