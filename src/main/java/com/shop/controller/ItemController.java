package com.shop.controller;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.service.ItemService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;


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

    @PostMapping("/admin/item/new")
    //이걸로 받으려면 뭐랑 맞아야하냐? 아이템 폼에 있는 저장버튼 쪽에 있는 @{/admin/item/new} 쪽에 있는거 form method가 post여야하고  enctype이 멀티파트 폼 데이터ㅇ여야함
    public String itemNew(@Valid @ModelAttribute ItemFormDto itemFormDto, BindingResult bindingResult, Model model, //화면에 보여줄게 있어서 모델 추가?
                          @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) { //ModelAttribute 생략 가능 생략하면 있는거로 침 얘가 자동 매핑해줌
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }
        //log.info("itemImgFileList={}", itemImgFileList);
        //log.info("itemFormDto={}", itemFormDto);
        //아이템폼디티오, 멀티파트 어쩌고

        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm"; //다시 폼 페이지로 돌아가게 함
        }

        try {
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생했습니다.");
            return "item/itemForm"; //상품 등록하는 페이지로 다시 보내줌
        }


        return "redirect:/";
    }

    @GetMapping("/admin/item/{itemId}")
    //이거느 동일하면 생략가능
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model) {
        //만약 들어온 아이템 아디가 디비에 존재하지 않아서 엔티티를 찾지 못한거에 대한 예외처리를 해줘야한다

        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);  //뷰로 데이터 전달해야함
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            return "item/itemForm";
        }
        return "item/itemForm";
    }

    @PostMapping("/admin/item/{itemId}")
    public String itemUpdate(@Valid @ModelAttribute ItemFormDto itemFormDto, BindingResult bindingResult, Model model, //화면에 보여줄게 있어서 모델 추가?
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {

        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }
        //등록일때만 첫번째 상품 이미지를 뺄 수 없게 하고 수정일떄는 첫번째 상품이 비어있어도 상관없다?
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm"; //다시 폼 페이지로 돌아가게 함
        }

        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생했습니다.");
            return "item/itemForm"; //상품 등록하는 페이지로 다시 보내줌
        }
        return "redirect:/";
    }

    @GetMapping(value={"/admin/items", "/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.orElse(0), 4);

        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "item/itemMng";

    }
}

