package com.back.domain.wiseSaying.dto;

import com.back.domain.wiseSaying.entity.WiseSaying;

import java.util.List;

public class PagingResult {
    private List<WiseSaying> items;
    private int totalSize;

    public PagingResult(List<WiseSaying> items, int totalSize){
        this.items = items;
        this.totalSize = totalSize;
    }

    public List<WiseSaying> getItems(){
        return this.items;
    }

    public int getTotalSize() {
        return totalSize;
    }
}
