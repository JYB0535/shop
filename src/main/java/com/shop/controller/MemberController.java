package com.shop.controller;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/new") //컨트롤러에서 리턴타입은 항상 String 이다 ==> public 'string'
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    //db에 값이 안 들어간건 이 메서드가 잘 안 됐다? 디버그 모드로 요청이 일단 컨트롤러까지는 도달하는지 확인해볼것
    //왜 안 되나? csrf 때문이다?. 우리 서버가 내려준게 맞다는걸 보증하기 위한걸 적어줘야한다 히든 인풋 사용한다?? html로 간다
    @PostMapping("/new")    //모델어트리뷰트 용도? 이러면 네임은 네임에 메일은 메일에 이름 똑같은 필드에 알아서 묶여서 들어간다? @
    public String memberForm(@ModelAttribute @Valid MemberFormDto memberFormDto,
                             BindingResult bindingResult, Model model) { //@Vaild 붙여두면 매핑 과정에서 안 맞으면 exception터짐
           //윗줄 @ModelAttribute이거 생략가능  //유저에게 받은 dto객체와 인코더(컨트롤러에서 주입받아야함) 패스워드 인코더는 빈으로 되어있기떄문에 위에  private final PasswordEncoder passwordEncoder;이거 추가해줌
        //뷰 어쩌고 때문에 마지막 매개변수에 모델추가
        if (bindingResult.hasErrors()) {
            return "member/memberForm"; //다시 입력하라고 멤버폼 페이지로 돌려보낸다(그쪽 뷰로)
        }

       try{ //이미 가입된 회원이면 서비스에서 exception터지는데 던짐 호출한쪽(컨트롤러로 ) 그래서 여기서 처리
           Member member = Member.createMember(memberFormDto, passwordEncoder);
           memberService.saveMember(member); //서비스 호출. 중복된 멤버가 없으면 잘 저장될것
       }catch (Exception e){                            //이미 가입된 회원입니다가 담겨있음
           model.addAttribute("errorMessage", e.getMessage());
           return "member/memberForm"; //회원가입 페이지로 이미 가입된 회원입니다를 다시 보낸다. 이거쓰고 폼으로 감 memberform

       }
        //BindingResult는 뭐 어떤게 충족하지 ㅇ못했는지 받아준다??  뷰가 연결된 컨트롤러에서 사용할때 바인딩 리절트 정보가 뷰에 자동으로 전달된다. 원래는 모델로 전달하는데 모델이 없어도 써주기만 하면 자동으로 전달된다? 반드시 @Vaild어노테이션 붙은거 *바로 뒤*에 써줘야함
        //BindingResult에 문제가 있으면 hasError
        //리다이렉트에서 슬래시로 가라고 하는데 받아줄곳이 없어서 컨트롤러로 간다.
        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginMember() {
        return "member/memberLoginForm"; //뷰 경로 명시할때 맨 앞에 슬래시 떼는게 나음 절대경로가 아니라 상대경로여야해서? members/memberLoginForm 맨앞에 슬래시 붙이지말라는거
    }

    @GetMapping("login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "member/memberLoginForm";
    }

}
