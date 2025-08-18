package com.shop.repository.item;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {

                //findItemByItemNm 이렇게도 쓸수있음
    List<Item> findByItemNm(String itemNm);
    /*
    SELECT *
    FROM item
    WHERE item_nm = ?
     */
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);
    /*
    SELECT *
    FROM item
    WHERE item_nm = ? OR item_detail = ?
     */

    List<Item> findByPriceLessThan(Integer price);
    /*
    SELECT *
    FROM item
    WHERE price < ?
     */

   List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);
   /*
   SELECT *
   FROM item
   WHERE price < ?
   ORDER BY price DESC
    */
                        //여기서 Item은 테이블 명이 아니라 class명을 써줘야함 JPQL이라서
    //FROM절 뒤에 들어가는 이름은 엔티티 CLASS 명
   @Query("SELECT i FROM Item i WHERE i.itemDetail LIKE %:itemDetail% ORDER BY i.price DESC")
   List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

                    //얘는 테이블 명이랑 같음
   @Query(value = "SELECT * FROM item i WHERE i.item_Detail LIKE %:itemDetail% ORDER BY i.price DESC", nativeQuery = true)
   List<Item> findByItemDetailByNative(String itemDetail);
}
