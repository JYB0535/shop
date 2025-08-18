package com.shop.service;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemImgDto;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.item.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final ItemImgService itemImgService;


    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        Item item = itemFormDto.createItem(); //이 메서드도 트랜잭션임
        itemRepository.save(item);

        for(int i=0; i<itemImgFileList.size(); i++) {
            //ItemImg 엔티티 생성
            //ItemImg 엔티티의 item 필드에 먼저 저장된 item을 set
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if(i == 0) { //이러면 한 아이템에 연관된 이미지 중에 딱 하나만 "y"로 저장 == > 이게 대표이미지 즉 첨부파일 첫번쨰로 들어온게 대표이미지
                itemImg.setRepImgYn("Y");
            }else {
                itemImg.setRepImgYn("N");
            }

            itemImgService.savaItemImg(itemImg, itemImgFileList.get(i));

        }

        return item.getId(); //자기 호출한 컨트롤러한테 돌려줌
    }

    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId) {
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for(ItemImg itemImg : itemImgList) {
            itemImgDtoList.add(ItemImgDto.of(itemImg));
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList); //디티오로 매핑한 리스트
        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception { //발생하는 exception은 다 컨트롤러에 몰아줌
        //itemFormDto 에는 id가 담겨 있음
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        //item ==> 더티체킹 . 아이템을 더티체킹 하려면 DTO에 있는 정보로 다 교체 해주면 된다.
        item.updateItem(itemFormDto);

        //아이템 엔티티 정보 반영 ==> 이미지 수정 내역 반영
        List<Long> itemImgIds = itemFormDto.getItemImgIds();
        for(int i=0; i<itemImgFileList.size(); i++) {
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }
        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }
}
