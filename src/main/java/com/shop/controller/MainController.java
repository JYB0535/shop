package com.shop.controller;


import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

//메인페이지로 가는 요청하나마 ㄴ처리할거임
@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;

    @GetMapping("/")
    public String main(ItemSearchDto itemSearchDto,
                       Optional<Integer> page,
                       Model model){
        Pageable pageable = PageRequest.of(page.orElse(0), 6);
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);
        return "main";
    }



    //    @GetMapping("/")
    //    public String main(Model model) {
    //        //예시로 만들었던거
    //       String imgSrc = "test.png";
    //       model.addAttribute("imgPath", imgSrc);
        //return "main"; //메인이라느  뷰 이름을 찾도록 다른 폴더 주소 없어서 템플릿츠 바로 밑에 만들어주면 된다
    //   }

}
