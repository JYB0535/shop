package com.shop.repository.cartItem;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.dto.CartDetailDto;
import lombok.RequiredArgsConstructor;

import java.util.List;
//큐 클래스 import
import static com.shop.entity.QCartItem.cartItem;
import static com.shop.entity.QItem.item;
import static com.shop.entity.QItemImg.itemImg;


@RequiredArgsConstructor
public class CartItemRepositoryCustomImpl implements CartItemRepositoryCustom {

    private final JPAQueryFactory jpaqueryFactory;

    @Override
    public List<CartDetailDto> findCartDetailDtoList(Long cartId) {
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

        return jpaqueryFactory
                .select(Projections.fields(CartDetailDto.class, //쿼리가 엔티티가 아니라 바로 dto로 반환되게 하려면 Projections 써야함
                        cartItem.id.as("cartItemId"),
                        item.itemNm,
                        item.price,
                        cartItem.count,
                        itemImg.imgUrl))
                .from(item)
                .join(cartItem)
                .on(cartItem.item.eq(item))
                .join(itemImg)
                .on(itemImg.item.eq(item))
                .where(cartItem.cart.id.eq(cartId))
                .where(itemImg.repImgYn.eq("Y"))
                .orderBy(cartItem.regTime.desc())
                .fetch();
    }
}
