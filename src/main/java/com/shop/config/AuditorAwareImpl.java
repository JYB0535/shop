package com.shop.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

                                        //이 제네릭이 영향 주는 곳? 구현할 메서드에서 문자열을 반환하게 한다.
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        /*
        AuditorAware ==> 엔티티 생성 및 수정 시에 해당 행위의 주체(유저)의 정보를 알아내는 역할
        구현 : Security Context - Authentication - 유저 정보 - 유저 아이디(이름) ==> 반환
         */

        //유저 아이디 여러겹인거 벗겨서 한개 찾아내서 반환
        //이게 기본
//        SecurityContext context = SecurityContextHolder. getContext();
//        Authentication authentication = context.getAuthentication();
//        String userId = authentication.getName();

        //but 인증안된 유저 등이면 null이 나올 수도 있음 nullpointerexception 혹은 db에 널이 들어갈수도 있음 그래서 안 전하게 하려면 String userId를 빈 문자 열로 초기화
        SecurityContext context = SecurityContextHolder. getContext();
        Authentication authentication = context.getAuthentication();
        String userId = "";
        if(authentication != null){
            userId = authentication.getName();
        }

        return Optional.of(userId);
    }

    //내가 만든거 빈으로 등록하고 싶다?
    /*
    빈을 붙여야하는데 뭘 붙여야하는지? configuration을 붙이기에는 성격이 다르다 원래 리포지토리 서비스 등 그냥 이름만 다르지 다 같은 빈이다? component안에 있음  이름만 다르고 다 같은 어노테이션 책에서는 configuration씀  근데 그냥 책대로 하기로 함 책이 정석이라고 함
     */
}
