// prototype.example.demo.config.XssFilterConfig.java
package prototype.example.demo.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Xssconfig {

    @Bean
    public FilterRegistrationBean<XssFilter> xssFilterRegistration() {
        FilterRegistrationBean<XssFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new XssFilter());
        registrationBean.addUrlPatterns("/*"); // 모든 URL에 필터를 적용
        registrationBean.setOrder(1); // 필터 우선순위를 가장 높게 설정 (가장 먼저 실행)
        return registrationBean;
    }
}