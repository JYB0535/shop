package com.shop.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

//메인페이지로 가는 요청하나마 ㄴ처리할거임
@Controller
public class MainController {

    @GetMapping("/")
    public String main(Model model) {
        String imgSrc = "test.png";
        model.addAttribute("imgPath", imgSrc);
        return "main"; //메인이라느  뷰 이름을 찾도록 다른 폴더 주소 없어서 템플릿츠 바로 밑에 만들어주면 된다
    }



}
