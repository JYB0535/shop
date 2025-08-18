package com.shop.entity;

import com.shop.repository.MemberRepository;
import com.shop.service.MemberService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberTest {

    @Autowired
    MemberService memberService;

    @PersistenceContext
    EntityManager em;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("Auditing 테스트")
    @WithMockUser(username = "gildong", roles = "USER") //이거 들어가야하는 이유? //이런 유저가 요청해서 실행 되는 것 처럼 동작한다.
    public void auditingTest() {
        Member member = new Member(); //여긴 아이디 지정 x
        memberRepository.save(member);

        em.flush(); //쿼리 바로 디비로 날아갈수 있게
        em.clear(); //영속성 컨텍스트 초기화

        Member savedMember = memberRepository.findById(member.getId()) //세이브 이후에 아이디 존재
                .orElseThrow(EntityNotFoundException::new);

        System.out.println("register time : " + savedMember.getRegTime());
        System.out.println("update time : " + savedMember.getUpdateTime());
        System.out.println("create member : " + savedMember.getCreatedBy());
        System.out.println("modify time : " + savedMember.getModifiedBy());




    }
}

