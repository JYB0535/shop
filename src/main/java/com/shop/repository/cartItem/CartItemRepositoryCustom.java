package com.shop.repository.cartItem;

import com.shop.dto.CartDetailDto;

import java.util.List;

public interface CartItemRepositoryCustom {

    /*
    SELECT ci.id, i.itemNm, i.price, ci.count, im.imgUrl //다 디티오에 필드로 존재하는 컬럼들만
    FROM cart_item ci
    INNER JOIN item i
        ON ci.item_id = i.item_id
    INNER JOIN item_img im
        ON i.item_id = im.item_id
    WHERE ci.cart_id = ?
        AND im.rep_img_yn = "Y"
    ORDER BY ci.regTime DESC


     */
    List<CartDetailDto> findCartDetailDtoList(Long cartId);
}
