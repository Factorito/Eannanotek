package prototype.example.demo.Filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import java.util.regex.Pattern;


public class XssFilter extends HttpServletRequestWrapper {

    private static final PolicyFactory POLICY_FACTORY = new HtmlPolicyBuilder().toFactory();

    public XssFilter(HttpServletRequest request) {
        super(request);
    }

    // 파라미터 값 정화
    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        return value == null ? null : sanitize(value);
    }

    // 파라미터 값 배열 정화
    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return null;
        }
        for (int i = 0; i < values.length; i++) {
            values[i] = sanitize(values[i]);
        }
        return values;
    }

    // 헤더 정화
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return value == null ? null : sanitize(value);
    }

    // OWASP Sanitizer를 사용하여 문자열을 정화하는 메서드
    private String sanitize(String value) {
        if (value == null || value.trim().isEmpty()) {
            return value;
        }
        return POLICY_FACTORY.sanitize(value);
    }
}