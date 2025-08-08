package com.shop.conpig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
//위 어노테이션이 달린 클래스에 @Bean이 어노테이션이 붙은 메서드를
//등록하면 해당 메서드의 반환 값(인스턴스)이 스프링 빈으로 등록됨
@EnableWebSecurity
public class SecurityConfig { //이게 있으면 시큐리티 기본 설정 안따르고 이거 없으면 기본 설정 따라서 로그인하라고 나옴
    /*config에 필수로 들어가야하는 빈 --> 스프링 시큐리티 필터 체인(스프링 시큐리티를 커스텀하기 위해서)*/
    //지금 뭘 넣어둔게 없어서 아무 동작도 안함(필터가 없는 상태)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin((it)-> it
                .loginPage("/members/login")
                .defaultSuccessUrl("/")
                .usernameParameter("email") //로그인하고 싶으면 로그인하는 폼에 이메일이라는 이름으로 줘라? 인풋 네임 속성에 이메일 넣어줘야된다. 그래야 폼 데이터에 아디 넣은게 이메일이라는 키값으로 들어간다
                .failureUrl("/members/login/error")
        );

        //얘 쓰면 더 이상 토큰 검사안함 지금 로그아웃하면 오류 생기는데 보안 정책 바뀌어서 a태그(get요청 말고 post요청으로 해야함) post에는 바디가 있어야하는데 거기에 토큰이 담겨있어야 멀쩡하게 된다
        //http.csrf((csrf)->csrf.disable());
       // http.csrf(AbstractHttpConfigurer::disable); //위에거랑 아래거랑 같음

        http.logout(logout-> logout
                .logoutUrl("/members/logout")
                .logoutSuccessUrl("/")



        );
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
