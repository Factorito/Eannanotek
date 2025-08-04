package prototype.example.demo.Filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

// 이 필터는 들어오는 요청을 감싸고 다음 필터로 전달하는 역할만 합니다.
public class XssFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // HTTP 요청인 경우에만 XssRequestWrapper로 감쌉니다.
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            // XssRequestWrapper를 사용하여 요청을 감싸고 다음 필터 체인으로 전달
            chain.doFilter(new XssRequestWrapper(httpServletRequest), response);
        } else {
            // HTTP 요청이 아니면 그대로 다음 필터로 전달
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 초기화 로직
    }

    @Override
    public void destroy() {
        // 소멸 로직
    }
}