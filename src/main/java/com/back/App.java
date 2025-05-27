package com.back;

import com.back.domain.wiseSaying.controller.WiseSayingController;
import com.back.domain.wiseSaying.dto.PagingResult;
import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.repository.WiseSayingRepository;
import com.back.domain.wiseSaying.service.WiseSayingService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    private final Scanner scanner = new Scanner(System.in);
    private final WiseSayingController wiseSayingController;

    public App(){
        WiseSayingRepository wiseSayingRepository = new WiseSayingRepository();
        WiseSayingService wiseSayingService = new WiseSayingService(wiseSayingRepository);
        this.wiseSayingController = new WiseSayingController(wiseSayingService);
    }

    boolean isExit = false;
    int lastIndex = 1;
    int getIndex;

    void run() {
        System.out.println("== 명언 앱 ==");
        if(wiseSayingController.getSize() == 0){
            makeSample();
        }

        while (true) {
            System.out.print("명령) ");
            String input = scanner.nextLine();

            String[] parts = input.split("\\?", 2);
            String cmd = parts[0];

            if(cmd.equals("등록")){
                actionWrite();
            }
            else if(cmd.equals("목록")){
                if(parts.length > 1) {
                    int pageVal = 1;
                    String page = extractParam("page", parts[1]);
                    String keywordType = extractParam("keywordType", parts[1]);
                    String keyword = extractParam("keyword", parts[1]);

                    if(page != null){
                        pageVal = Integer.parseInt(page);
                    }

                    actionListByParam(pageVal, keywordType, keyword);
                }
                else{
                    actionListByParam(1, null, null);
                }
            }
            else if(cmd.equals("수정")){
                if(parts.length > 1){
                    getIndex = extractId(parts[1]);
                    if(getIndex == -1){
                        System.out.println("잘못된 id 입력입니다.");
                        continue;
                    }
                    actionModify(getIndex);
                }
            }
            else if(cmd.equals("삭제")){
                if(parts.length > 1){
                    getIndex = extractId(parts[1]);
                    if(getIndex == -1){
                        System.out.println("잘못된 id 입력입니다.");
                        continue;
                    }
                    actionDelete(getIndex);
                }
            }
            else if(cmd.equals("빌드")){
                actionBuild();
            }
            else if(cmd.equals("종료")){
                break;
            }
        }
    }

    void makeSample(){
        for(int i = 1; i <= 10; i++){
            wiseSayingController.write("명언 " + i, "작자미상 " + i);
        }
        System.out.println("명언 데이터가 0개 입니다. 샘플 데이터가 생성되었습니다.");
    }

    void actionWrite(){
        System.out.print("명언 : ");
        String content = scanner.nextLine();
        System.out.print("작가 : ");
        String author = scanner.nextLine();

        WiseSaying wiseSaying = wiseSayingController.write(content, author);
        System.out.println(wiseSaying.id + "번 명언이 등록되었습니다.");
    }

    void actionListByParam(int page, String keywordType, String keyword) {
        List<WiseSaying> wiseSayings = wiseSayingController.list(page, keywordType, keyword);

        if(wiseSayings.isEmpty()){
            System.out.println("항목이 존재하지 않습니다.");
            return;
        }

        int maxPage = ((wiseSayings.size() - 1) / 5) + 1;
        if(page > maxPage) {
            System.out.println("잘못된 페이지 입력입니다.");
            return;
        }

        int startPos = (page-1) * 5;
        int endPos = page * 5;

        if(endPos > wiseSayings.size()){
            endPos = wiseSayings.size();
        }

        List<WiseSaying> wiseSayingsSubList = wiseSayings.subList(startPos, endPos);

        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        if(keywordType != null){
            System.out.println("검색타입 : " + keywordType);
            System.out.println("검색어 : " + keyword);
            System.out.println("----------------------");
        }

        for(WiseSaying ws : wiseSayingsSubList){
            ws.printWiseSaying();
        }

        System.out.println("----------------------");
        System.out.print("페이지 : ");
        for(int i = 1; i <= maxPage; i++){
            if(i == page){
                System.out.print("[" + i + "]");
            }
            else{
                System.out.print(i);
            }

            if(i != maxPage){
                System.out.print(" / ");
            }
        }
        System.out.println();
    }

    void actionBuild(){
        wiseSayingController.build();
        System.out.println("data.json 파일의 내용이 갱신되었습니다.");
    }

    void actionDelete(int id){
        boolean isDelete = wiseSayingController.delete(id);
        if(isDelete){
            System.out.println(id + "번 명언이 삭제되었습니다.");
        }
        else{
            System.out.println(id + "번 명언은 존재하지 않습니다.");
        }
    }

    void actionModify(int id) {
        WiseSaying wiseSaying = wiseSayingController.find(id);
        if(wiseSaying.id == -1){
            System.out.println(id + "번 명언은 존재하지 않습니다.");
        }
        else{
            System.out.println("명언(기존) : " + wiseSaying.content);
            System.out.print("명언 : ");
            String newContent = scanner.nextLine();

            System.out.println("작가(기존) : " + wiseSaying.author);
            System.out.print("작가 : ");
            String newAuthor = scanner.nextLine();

            wiseSayingController.modify(id, newContent, newAuthor);
        }
    }

    private int extractId(String input) {
        Pattern pattern = Pattern.compile("id=(\\d+)");
        Matcher matcher = pattern.matcher(input);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : -1;
    }

    private String extractParam(String Type, String input){
        Pattern pattern = Pattern.compile(Type + "=([^&]+)");
        Matcher matcher = pattern.matcher(input);
        return matcher.find() ? matcher.group(1) : null;
    }
}
