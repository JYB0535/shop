package com.shop.service;


import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor

public class ItemImgService {

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private  final FileService fileService;

    /*
    얘 책임은 뭐냐?
    클라이언트(브라우저)가 보낸 item 이미지 정보, 첨부파일
    ==> 요청 바디에서 데이터를 자바 객체로 바인딩 하는 책임 ==> 컨트롤러 책임, 서비스의 책임이 아님

    서비스 책임 ?
    1. MultipartFile 에 들어있는 이미지 파일을 ==> 파일시스템에 저장 ==> FileService 호출 하면 된다.
    2. DB에 저장할 ItemImg 정보 ==> DB에 저장 ==> ItemImgRepository 시키면 된다.

    서비스 할 일 받은거 적당한데에 보내주는거
    1번 2번 순서 중요 왜? 트랜잭션으로 묶여있어서 --> 파일을 먼저 저장하고 그 파일 정보를 db에 저장해야한다.
    파일이 저장되지 않으면 db 에도 insert 되면 안된다.
     */
                                                //첨부 파일 형식
    public void savaItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {

        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName ="";
        String imgUrl ="";
                                            //이거는 바로 위에 있고  //이거는 멀티파트 파일에 있고
        //들어가야 할 정보 --> 저장할 업로드 경로, 원본의 파일 이름, 파일 바이너리 배열
        //업로드 경로는 properties에 있음 값 들고 오려면 @Value 써야함


        //1번.파일 시스템에 파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            imgUrl = "/images/item/" + imgName;
        }

        itemImg.updateItemImg(oriImgName, imgName, imgUrl);

        //2번.DB에 저장된 파일 정보 저장
        itemImgRepository.save(itemImg);
    }

    //업로드랑 비슷한데 다른 점은 디비에서 하나를 딱 집어서 있든 비어있든 덮어쓴다고 생각
    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {
        if(!itemImgFile.isEmpty()) {
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new);

            //1. 기존에 파일시스템에 저장되어있던 이미지 삭제
            if(!StringUtils.isEmpty(savedItemImg.getImgName())){//이게 비어있으면 저장된 파일이 없다는뜻 지금은 비어있지 않으면임
                fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName()); //기존에 저장되어있던거 지워줌
            }
            //2. DB에 기존 레코드 내용 업데이트 (새로 저장된 이미지의 경로, 원본 이미지 이름) <-- 다르니까 업데이트 해줘야함
            // JPA 에서 Entity Update ==> dirty checking ==> 조회된 엔티티의 필드 변경
            String oriImgName = itemImgFile.getOriginalFilename();
            //새 파일 저장하고 저장된 파일명 받기
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/item/" + imgName;
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
            //얘는 반환 타입 없음
        }
    }
}
