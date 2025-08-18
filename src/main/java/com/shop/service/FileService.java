package com.shop.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Slf4j
@Service
public class FileService {

    /*
    uploadPath : 파일을 저장할 위치(파일 시스템)
    originalFileName : 유저가 첨부한 파일의 원래 이름
    fileData : 파일의 바이너리 데이터(원본)

    <<purpose>>
    파일을 파일시스템에 저장
    originalFileName 그대로 저장하면 중복(덮어쓰기) 위험
    그래서 originalFileName ==> 중복 위험에 안전한(UUID를 적용한)이름으로 변경해서 저장
     */

    //파일 업로드 메서드
    public String uploadFile(String uploadPath, String originalFileName,
                             byte[] fileData) throws Exception {
        /* originalFileName을  ==> UUID가 적용된 이름으로 변경
        1. UUID 생성
        2. originalFileName 에서 파일 확장자 얻기
        */
        UUID uuid = UUID.randomUUID();
        //ex) test.png 같은 경우 뒤엥 있는 png만 받아내는거는
        //te.st.png ==> .png
        //(test.png).substring(.이 있는 인덱스 숫자) ==> png
        String extension =originalFileName.substring(originalFileName.lastIndexOf('.'));
        String savedFileName = uuid.toString() + extension;
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
        log.info("방금 저장된 파일명 : {}", savedFileName);
        // 파일 시스템에 저장할 위치(fileUploadFullUrl)와 STREAM  연결
                            //파일 아웃풋 스트림(저장할 파일 경로(파일명 까지 포함));
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        //연결된 STREAM 통해 데이터르 ㄹ저장
        fos.write(fileData);
        fos.close();
        return savedFileName; //문자열 리턴해야하는데 저장된 파일명 리턴
    }

    //파일 삭제
    public void deleteFile(String filePath) throws Exception {
        File deleteFile = new File(filePath);
        if (deleteFile.exists()) {
            deleteFile.delete();         //파일 경로를 파일 객체로 감싸서 딜리트 메서드 호출하면 삭제된다.
            log.info("[파일 삭제] : {}", filePath);
        }else {
            //유저한테까지는 알림 줄 필요 없고 우리 디버그 할때 편하게 출력 줄건데 sysout 은 사용하면 안된다
            log.info("[존재하지 않는 파일] : {}", filePath);
        }

        //없는 경로 입력하면 exception 생길 건데.

    }
}
