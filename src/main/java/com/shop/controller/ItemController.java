package com.shop.controller;

import com.shop.dto.ItemFormDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ItemController {
    @GetMapping("/admin/item/new")
    public String itemForm(Model model) {  //필드 일관성 맞춰주려고 빈 객체 내려줌 model
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "/item/itemForm";

        //이렇게 객체 채워주면 itemform에 있는거 ㅜ머 3개 어쩌고
//        ItemFormDto itemFormDto = new ItemFormDto();
//        itemFormDto.setItemNm("test name");
//        model.addAttribute("itemFormDto", itemFormDto);
//        return "/item/itemForm";
    }


}
