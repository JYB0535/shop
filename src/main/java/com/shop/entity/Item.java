package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "item") //데이터 베이스에 만들 테이블 명이랑 이름 같게 하면 된다
@Getter
@Setter
@ToString
public class Item extends BaseEntity {
    @Id
    @Column(name ="item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String itemNm;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer stockNumber;

    @Lob
    @Column(nullable = false)
    private String itemDetail;

                // db에 저장할 때 문자열로 저장한다. enum은 자바의 개념이라서 db는 모름 문자열로 값 주고 받는다.
    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemNm = itemFormDto.getItemNm(); //this에 item네임 디티오에 있는거로 업데이트
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();

    }

//    private LocalDateTime regTime;
//
//    private LocalDateTime updateTime;
}
