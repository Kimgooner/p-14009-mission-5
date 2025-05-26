package com.back.domain.wiseSaying.service;

import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.repository.WiseSayingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WiseSayingService {
    private final WiseSayingRepository wiseSayingRepository;
    private int lastId;

    public WiseSayingService(WiseSayingRepository repository){
        this.wiseSayingRepository = repository;
        this.lastId = repository.loadLastId();
    }

    public WiseSaying create(String content, String author){
        WiseSaying wiseSaying = new WiseSaying(lastId, content, author);
        wiseSayingRepository.saveFile(wiseSaying);
        wiseSayingRepository.saveLastId(++lastId);
        return wiseSaying;
    }

    public List<WiseSaying> findByKeyword(String type, String key){
        List<WiseSaying> wiseSayings = wiseSayingRepository.loadAll();
        List<WiseSaying> filtered = new ArrayList<>();

        if(type.equals("content")){
            filtered = wiseSayings.stream()
                    .filter(w -> w.content.contains(key))
                    .collect(Collectors.toList());
        }
        else if(type.equals("author")){
            filtered = wiseSayings.stream()
                    .filter(w -> w.author.contains(key))
                    .collect(Collectors.toList());
        }

        return filtered;
    }

    public List<WiseSaying> findAll(){
        return wiseSayingRepository.loadAll();
    }

    public void makeBuild(){
        wiseSayingRepository.saveBuild();
    }

    public WiseSaying findById(int id){
        return wiseSayingRepository.loadById(id);
    }

    public boolean deleteById(int id){
        return wiseSayingRepository.delete(id);
    }

    public void modifyWiseSaying(int id, String newContent, String newAuthor){
        WiseSaying wiseSaying = new WiseSaying(id, newContent, newAuthor);
        wiseSayingRepository.saveFile(wiseSaying);
    }
}
