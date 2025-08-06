package prototype.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers(

                        "/prototype/register",
                        "/prototype/send-security-code"
                ))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/",
                                "/prototype/main",
                                "/prototype/login",
                                "/prototype/register",
                                "/prototype/send-security-code",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/Eannanotek.png",
                                "/favicon.ico",
                                "/prototype/mi",
                                "/prototype/mi2",
                                "/prototype/mi3"

                        ).permitAll()
                        .requestMatchers("/prototype/qeq1", "/prototype/qeq2", "/prototype/submit","/prototype/board/submit").authenticated()
                        .requestMatchers("/write/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/prototype/login")
                        .loginProcessingUrl("/authenticate")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/prototype/main", true)
                        .failureUrl("/prototype/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/prototype/logout")
                        .logoutSuccessUrl("/prototype/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
}