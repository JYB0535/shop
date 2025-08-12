package com.shop.entity;

import com.shop.dto.MemberFormDto;
import com.shop.repository.CartRepository;
import com.shop.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
//스프링 빈 주입 받아서 테스트 할거임?
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class CartTest {
    //멤버 엔티티를 만들어서 반환하는 메서드

    //장바구니 회원 조회 테스트에서 카트 리포지토리랑 멤버 리포지토리 필요함
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MemberRepository memberRepository;

    //엔티티 매너저 주입받는 방법
    @PersistenceContext
    EntityManager em;

    //인코더는 스프링 빈으로 뭐?
    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@test");
        memberFormDto.setName("test");
        memberFormDto.setAddress("창원시");
        memberFormDto.setPassword("123456");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
    public void findCartAndMemberTest() {
        /*
        1. 신규 회원 등록
        2. 해당 회원 엔티티의 장바구니 생성 및 등록
        3. 저장된 장바구니를 통해 해당 회원 조회
        4. 조회된 회원과 저장한 회원 정보가 일치하는지 확인
        */

            //1. 저장
        Member member = createMember(); //저장한 정보를 멤버 엔티티에 받아옴
        memberRepository.save(member); //정보 저장

        //카트는 봤을때 아이디는 자동이라서 넣어줄값은 멤버밖에 엇ㅂ음
        Cart cart = new Cart();
        cart.setMember(member); //객체 관점에서는 멤버 필드 타입이 멤버 클래스. 객체를 넣어줘야허ㅏㄴ다?? 멤버를 그대로? 그렇게 해야 저장ㄷ이 된다?
        cartRepository.save(cart); //멤버 품고 있는 카트 저장

                //책에 194페이지에 보면 flush()와 clear() 해줌 왜?  Member member = createMember(); Cart cart = new Cart(); 이걸로 영속성 콘텍스트에 cart랑 member가 있음 캐싱되어있다. 2-4그림 찹ㅁ조
                // 이렇게 하면 근데 그냥 자바안에서 가져오는거지 데이터베이스에서 가져오는게 아님(조회쿼리가 날아가지 않음)
                //한번 비워야 캐시 미스가 나고 진짜 db에 조회 쿼리를 날린다.

        //앞에 영속성 컨텍스트? 있는 그림 그림 2-3? 2-4?
        System.out.println("저장하기 전 cart의 주소값 : " + cart.hashCode());
        System.out.println("저장하기 전 member의 주소값 : " + member.hashCode());

        em.flush(); //지연쓰기 저장소에 있는 sql 다 내려보냄
        em.clear(); //1차 캐시에 있는 것들 다 바깥으로 떼어보냄


            //2. 조회
            //여기에 담아줌                 // 저장되서 만들어진 아이디로 조회함
        Optional<Cart> savedCartOp = cartRepository.findById(cart.getId());
        //Cart savedCart = cartRepository.findById(cart.getId()); 이렇게는 못함 왜? findById는 내가 만든게 아님 그래서 값이 있을수도 없을 수도 있음 그래서 optional로 받아줘야함(널이 있을 수도 있다.)

        //옵셔널 안에 값이 존재하면 꺼내서 받고, null이면 EntityNotFoundException 던지기 값이 있으면 왼쪽에 던져주고 아니면 exception
        //Cart savedCart = savedCartOp.orElseThrow(() -> new EntityNotFoundException());
        Cart savedCart = savedCartOp.orElseThrow(EntityNotFoundException::new);

        //회원꺼내기 카트와 연관관계 있느 ㄴ멤버가 있잖음

            //DB관점
        /*
        1. 카트를 조회한다.
        2. 조회된 카트 레코드에서 member_id FK값을 얻는다.
        3. 얻은 FK를 가지고 member 테이블에서 연관된 유저를 찾는다.

        또는

        1. cart테이블과 member테이블 사이에 member_id FK를 조건으로 JOIN한다.
         */

        // 이거는 객체(JPA) 관점 이게 JPA의 핵심이다? 우리가 조인 안하고 우리가 카트 조회하는 순간 카트에 걸려있는 연관관계 떄문에 JOIN 쿼리가 날아간다. 그래서 카트 안에서 바로 멤버 꺼내ㅆ쓸수 있다?
        Member foundMember = savedCart.getMember();
        foundMember.getEmail();

        System.out.println("저장하고난 후 cart의 주소값 : " + savedCart.hashCode());
        System.out.println("저장하고난 후 member의 주소값 " + foundMember.hashCode());
        //테스트 통과조건
        assertEquals(foundMember.getId(), member.getId());


    }

}