package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainItemDto {

    private  Long id;

    private String itemNm;  //아이템 이름

    private String itemDetail;

    private Integer price;

    private String imgUrl;


}
