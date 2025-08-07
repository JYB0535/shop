package com.shop.service;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import javassist.bytecode.DuplicateMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    //파이널 붙은 필드에는 위에 @Requir어쩌고 붙여야함 그래야 파이널 붙은거만 생성자 만들어준다?
    private final MemberRepository memberRepository;

    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    //중복된 멤버가 있는지 검증
    private void validateDuplicateMember(Member member) {
        Member foundMember = memberRepository.findByEmail(member.getEmail());
        if (foundMember != null) {
            throw new IllegalArgumentException("이미 가입된 회원입니다.");
        }

    }

}
