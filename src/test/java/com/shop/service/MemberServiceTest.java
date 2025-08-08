package com.shop.service;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest //이거 써야 빈 주입받음
@Transactional //이건 실제 사용하는 db에 연결했을때 테스트로 발생한 데이터를 롤백시켜주는 기능 ㄲ곡 없어도 된다 어차피 h2라는 임시 디비 사용할거라
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberServiceTest {

        @Autowired
        MemberService memberService;

        @Autowired //필드 만들어서 주입
        PasswordEncoder passwordEncoder;

        public Member createMember(){
            MemberFormDto memberFormDto = new MemberFormDto();
            memberFormDto.setEmail("test@test.com");
            memberFormDto.setName("test");
            memberFormDto.setPassword("test");
            memberFormDto.setAddress("창원시 의창구 사림동");
                                                    //스프링 빈이라서 잘 주입된다.
            return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 테스트")
    void saveMember() {
            Member member = createMember(); //1.저장하려고 만든 멤버 정보
            Member savedMember = memberService.saveMember(member); //2.저장되고 반환된 멤버 정보

        //1이랑 2랑 비교
        assertEquals(member.getEmail(), savedMember.getEmail());
        assertEquals(member.getName(), savedMember.getName());
        assertEquals(member.getAddress(), savedMember.getAddress());
        assertEquals(member.getPassword(), savedMember.getPassword());
        assertEquals(member.getRole(), savedMember.getRole());

        System.out.println(savedMember);

    }

    @Test
    @DisplayName("중복 회원가입 테스트")
    public void saveDuplicateMemberTest(){
            Member member1 = createMember();
            Member member2 = createMember();
            memberService.saveMember(member1);

            Throwable e = assertThrows(IllegalArgumentException.class, () -> {
                memberService.saveMember(member2);});

            assertEquals("이미 가입된 회원입니다.", e.getMessage());
    }

}