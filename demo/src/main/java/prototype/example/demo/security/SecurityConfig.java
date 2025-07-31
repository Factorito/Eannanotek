package prototype.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import prototype.example.demo.Filter.SecurityFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilter securityFilter() {
        return new SecurityFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityFilter securityFilter) throws Exception {

        http
                .cors(cors -> {})
                .csrf(csrf -> {
                    csrf.csrfTokenRepository(new HttpSessionCsrfTokenRepository());
                    csrf.csrfTokenRequestHandler(new XorCsrfTokenRequestAttributeHandler());
                })

                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)

                .authorizeHttpRequests(authorize -> authorize
                        // 메인 페이지, 정적 리소스, 로그인, 회원가입 페이지는 누구나 접근 가능
                        .requestMatchers("/", "/public/**", "/resources/**", "/login", "/register").permitAll()

                        // '/prototype/main' 경로도 인증 없이 접근 가능하게 허용
                        .requestMatchers("/prototype/main").permitAll()

                        // CQ_QEQ.HTML 페이지와 관련된 경로는 인증된 사용자만 접근 가능
                        .requestMatchers("/prototype/qeq").authenticated()
                        .requestMatchers("/prototype/submit").authenticated()

                        // '/admin/**' 경로는 관리자 권한을 가진 사용자만 접근 가능
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // 그 외 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/authenticate")
                        .defaultSuccessUrl("/prototype/main", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll()
                );

        return http.build();
    }
}