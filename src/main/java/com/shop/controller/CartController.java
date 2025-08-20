package com.shop.controller;

import com.shop.dto.CartDetailDto;
import com.shop.dto.CartItemDto;
import com.shop.dto.CartOrderDto;
import com.shop.service.CartService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    //얘도 비동기 동작
    @PostMapping("/cart")
    public ResponseEntity<?> order(@Valid @RequestBody CartItemDto cartItemDto,
                                   BindingResult bindingResult, //BindingResult에 필드 에러에 대한 정보들이 담긴다. BindingResult는 valid 하는거 바로 뒤에 붙여줘야함
                                   Authentication authentication) {
        if (bindingResult.hasErrors()) {
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

        Long cartItemId;

        try{
            cartItemId = cartService.addCart(cartItemDto, authentication.getName());
        }catch (EntityNotFoundException e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(cartItemId);
    }

    @PatchMapping("/cartItem/{cartItemId}") // requestParam 은 ?count = 이렇게 생긴거
    public ResponseEntity<?> updateCartItem(@PathVariable Long cartItemId,
                                            @RequestParam int count) {
        cartService.updateCartItemCount(cartItemId, count);
        return ResponseEntity.ok().body(cartItemId);

    }

    @GetMapping("/cart")
    public String orderHist(Authentication authentication,
                            Model model) {
        List<CartDetailDto> cartDetailDtoList =
                cartService.getCartList(authentication.getName());
        model.addAttribute("cartItems", cartDetailDtoList);
        return "cart/cartList";
    }

    @DeleteMapping("/cartItem/{cartItemId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long cartItemId) {
        cartService.deleteCartItem(cartItemId);
        return ResponseEntity.ok().body(cartItemId);
    }

    @PostMapping("/cart/orders")
    public ResponseEntity<?> orders(@RequestBody CartOrderDto cartOrderDto,
                                    Authentication authentication) {
        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();
        if(cartOrderDtoList.isEmpty()) {
            return new ResponseEntity<String>("주문할 상품을 선택해주세요", HttpStatus.FORBIDDEN);
        }
        Long orderId = cartService.orderCartItems(cartOrderDtoList, authentication.getName());
        return ResponseEntity.ok().body(orderId);
    }




}
