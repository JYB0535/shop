package com.shop.controller;

import com.shop.dto.OrderDto;
import com.shop.dto.OrderHistDto;
import com.shop.exception.OutOfStockException;
import com.shop.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor //최소 서비스빈은 주입받을거라서 이것도 필요하다?
public class OrderController {

    private final OrderService orderService;

    //주문했을때 비동기로 ㅊㅓ리할거다??


    //Principal principal, Authentication authentication 둘 다 유저 정보 꺼내올 수 있다 별 차이 없음?
    @PostMapping("/order")
    @ResponseBody  //리턴값이 json방식으로 바로 응답하겠다??
    public ResponseEntity<?> order(@RequestBody @Valid OrderDto orderDto, //(@RequestBody) < -- 폼데이터가 아니다. d요청바디에서 꺼낼때는 request바디 응답 제이슨으로 줄때는 reponse바디
                                BindingResult bindingResult,
                                //Principal principal,
                                Authentication authentication) {
        //유효성 검사쪽 처리?? 맞는지 모르겠음
        if(bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            //에러 메시지 붙여서 출력해줄건데 그냥 string + 하면 너무 메모리 많이 잡아먹는다? 그래서 스트링 빌더 사용
            StringBuilder sb = new StringBuilder();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getField())
                        .append(": ")
                        .append(fieldError.getDefaultMessage())
                        .append("\n"); //여러줄이니까 줄 바꿈 해줌
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        Long orderId = null;
        try{                                            //이메일 정보
            orderId = orderService.order(orderDto,authentication.getName()); //누가 뭐를 얼만큼 주문하는지 매개변수로 줘야함 //재고가 없어서 exception 터질거 대비??
        }catch(OutOfStockException e){        //바디에 들어갈 값 //상태 코드
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().body(orderId);

    }

    @GetMapping(value = {"/orders", "orders/{page}"})
    public String orderHist(@PathVariable Optional<Integer> page,
                            Authentication authentication,
                            Model model) {
                                            //내가 보고 싶은 페이지 넘버, //한 페이지당 보여줄 수
        Pageable pageable = PageRequest.of(page.orElse(0), 4);
        Page<OrderHistDto> orderHistDtoList =
                orderService.getOrderHist(authentication.getName(), pageable);
        //모델로 하나씩 넘김            // 이름 너무 길면 불편하니까 바꿈 원래  orderHistDtoList였음
        model.addAttribute("orders", orderHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);
        return "order/orderHist"; //뷰로 보내줌
    }
}
