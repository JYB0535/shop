package com.shop.repository;

import com.shop.entity.ItemImg;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    /*
    FK인 item_id로 ItemImg를 조회
    SELECT *
    FROM item_img
    WHERE item_id = ?
    ORDER BY item_img_id ASC
    */

    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

    //이거는 간단하니까 쿼리 메서드로 쓴다
    ItemImg findByItemIdAndRepImgYn(Long itemId, String repImgYn);

}
