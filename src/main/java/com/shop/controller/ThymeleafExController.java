package com.shop.controller;

import com.shop.dto.ItemDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/thymeleaf")
public class ThymeleafExController {

    @GetMapping("/ex01")  //여기 안에잇는건 주소가 thymeleaf/ex01 이런식으로 감
    public String thymeleafExample01(Model model) {//controller는 반환타입이 String 이어야함 //매개변수로 주입받은 모델에 값을 넣어놓으면 view 까지 저낟ㄹ이 왼다.
        model.addAttribute("data", "타임리프 예제 입니다");
        return "thymeleafEx/thymeleafEx01";
        //바로 윗줄 앞에 생략된거  --> /resources/templates
    }

    @GetMapping("/ex02")
    public String thymeleafExample02(Model model) {
        ItemDto itemDto = new ItemDto();
        itemDto.setItemDetail("상품 상세 설명");
        itemDto.setItemNm("테스트 상품1");
        itemDto.setPrice(10_000);
        itemDto.setRegTime(LocalDateTime.now());

        model.addAttribute("itemDto", itemDto);
        return "thymeleafEx/thymeleafEx02";

    }

    @GetMapping("/ex03")
    public String thymeleafExample03(Model model) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ItemDto itemDto = new ItemDto();
            itemDto.setItemDetail("상품 상세 설명" + i);
            itemDto.setItemNm("테스트 상품" + i);
            itemDto.setPrice(1000 + i);
            itemDto.setRegTime(LocalDateTime.now());

            itemDtoList.add(itemDto);
        }                           //타임리프에서 찾을 이름 //위에서 만든 리스트의 이름
        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleafEx/thymeleafEx03";
    }

    @GetMapping("/ex04")
    public String thymeleafExample04(Model model) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ItemDto itemDto = new ItemDto();
            itemDto.setItemDetail("상품 상세 설명" + i);
            itemDto.setItemNm("테스트 상품" + i);
            itemDto.setPrice(1000 + i);
            itemDto.setRegTime(LocalDateTime.now());

            itemDtoList.add(itemDto);
        }                           //타임리프에서 찾을 이름 //위에서 만든 리스트의 이름
        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleafEx/thymeleafEx04";
    }

    @GetMapping("/ex05")
//    public String thymeleafExample05() {
//        return "thymeleafEx/thymeleafEx05";
    public String thymeleafExample05(Model model) {
        String param1 = "스프링 부트";
        String param2 = "타임 리프";
        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);
        return "thymeleafEx/thymeleafEx05";
    }

    @GetMapping("/ex06")   //데이터 받아내는건 매개변수로 받는 거여서 매개변수 넣어야함 //URL에 달리는거 @RequestParam //모델도 넣어줌 왜지?
    public String thymeleafExample06(
            @RequestParam String param1,
            @RequestParam String param2,
            Model model) {
        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);
        return "thymeleafEx/thymeleafEx06";
    }

    @GetMapping("/ex07")
    public String thymeleafExample07() {
        return "thymeleafEx/thymeleafEx07";
    }
}