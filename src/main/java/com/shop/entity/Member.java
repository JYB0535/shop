package com.shop.entity;


import com.fasterxml.jackson.databind.ser.Serializers;
import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "member")  //db테이블 연동을 위해
@Getter
@Setter
@ToString
public class Member extends BaseEntity {//repository에 저장하려면 entity가 필요함

    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String name; //컬럼명이 필드랑 같으면 되면 @column 안 붙여도 된다

    @Column(unique = true)  //구분은 id로 하는데 로그인은 email로 할거 그래서 중복 없이
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING) //db에 문자열로 저장
    private Role role;

    //디티오로 받아서 엔티티로 저장해야함 그 과정에서 인코딩 할거임??      //스프링빈은 스프링 빈끼리만 주입 가능해서 passwordEncoder 스프링 빈이어도 주입 불가 밖에서 받아 들어와야함
    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        //깃 테스트
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());       //passwordEncoder에 인코드라는 메서드 있음
//        member.setPassword(passwordEncoder.encode(memberFormDto.getPassword()));
        //위에 한줄이랑 아래 두줄이랑 같음 그냥 나눠서 쓴거
        String encodedPw = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(encodedPw);
        member.setRole(Role.USER);
        return member;
    }



}
