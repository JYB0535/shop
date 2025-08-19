package com.shop.dto;

import com.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {
    //하나의 주문에 상품 여러개가 있을 수 있음
    //일단 상품하나의 정보에 대한거부터 기입

    //생성자. 값 받아서 넣어줌
    public OrderItemDto(OrderItem orderItem, String imgUrl) { // Item말고 OrderDto여야하는게 주문했을 당시 가격과 등등이 필요해서
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;

    }

    private String itemNm;
    private int count;
    private int orderPrice;
    private String imgUrl;



}
