package com.shop.conpig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
//위 어노테이션이 달린 클래스에 @Bean이 어노테이션이 붙은 메서드를
//등록하면 해당 메서드의 반환 값(인스턴스)이 스프링 빈으로 등록됨
@EnableWebSecurity
public class SecurityConfig {
    /*config에 필수로 들어가야하는 빈 --> 스프링 시큐리티 필터 체인(스프링 시큐리티를 커스텀하기 위해서)*/
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /*
        http.설정1
        http.설정2
        http.설정3
        */
        return http.build();

    }
    //민감한 데이터 암호화(인코딩 해주는 빈)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //얘가 스프링 빈
    }
}
