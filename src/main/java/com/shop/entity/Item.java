package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDto;
import com.shop.exception.OutOfStockException;
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

    public void removeStock(int stockNumber) {
        int restStock = this.stockNumber - stockNumber; //원래 재고에 매개변수로 받은(지우고자 하는 수량) 빼줌
        if(restStock < 0) {
            throw new OutOfStockException("상품의 재고가 부족합니다." + "(현재 재고 수량 : " + this.stockNumber + ")");
        }

        this.stockNumber = restStock;
    }

//    private LocalDateTime regTime;
//
//    private LocalDateTime updateTime;
}
