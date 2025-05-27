package com.back.domain.wiseSaying.controller;

import com.back.domain.wiseSaying.dto.PagingResult;
import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.service.WiseSayingService;

import java.util.List;

public class WiseSayingController {
    private final WiseSayingService wiseSayingService;

    public WiseSayingController(WiseSayingService wiseSayingService){
        this.wiseSayingService = wiseSayingService;
    }

    public WiseSaying write(String content, String author){
        return wiseSayingService.create(content, author);
    }

    public List<WiseSaying> list(int page, String keywordType, String keyword) {
        if(keywordType != null){
            return wiseSayingService.findByKeyword(page, keywordType, keyword);
        }
        else{
            return wiseSayingService.findAll(page);
        }
    }

    public int getSize(){
        return wiseSayingService.getSize();
    }

    public boolean delete(int id){
        return wiseSayingService.deleteById(id);
    }

    public WiseSaying find(int id){
        return wiseSayingService.findById(id);
    }

    public void modify(int id, String newContent, String newAuthor){
        wiseSayingService.modifyWiseSaying(id, newContent, newAuthor);
    }

    public void build(){
        wiseSayingService.makeBuild();
    }
}
