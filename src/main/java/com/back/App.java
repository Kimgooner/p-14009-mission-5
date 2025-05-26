package com.back;

import com.back.domain.wiseSaying.controller.WiseSayingController;
import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.repository.WiseSayingRepository;
import com.back.domain.wiseSaying.service.WiseSayingService;

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

        while (true) {
            System.out.print("명령) ");
            String input = scanner.nextLine();

            if (input.equals("등록")) {
                actionWrite();
            } else if (input.equals("목록")) {
                actionList();
            } else if (input.equals("종료")) {
                break;
            } else if (input.equals("빌드")) {
                actionBuild();
            }
            else if (input.startsWith("삭제?id=")) {
                getIndex = extractId(input);
                if(getIndex == -1){
                    System.out.println("잘못된 id 입력입니다.");
                    continue;
                }
                actionDelete(getIndex);
            } else if (input.startsWith("수정?id=")) {
                getIndex = extractId(input);
                if(getIndex == -1){
                    System.out.println("잘못된 id 입력입니다.");
                    continue;
                }
                actionModify(getIndex);
            }
        }
    }

    void actionWrite(){
        System.out.print("명언 : ");
        String content = scanner.nextLine();
        System.out.print("작가 : ");
        String author = scanner.nextLine();

        WiseSaying wiseSaying = wiseSayingController.write(content, author);
        System.out.println(wiseSaying.id + "번 명언이 등록되었습니다.");
    }

    void actionList() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");
        List<WiseSaying> wiseSayings = wiseSayingController.list();

        wiseSayings.sort((a, b) -> Integer.compare(b.id, a.id));

        for(WiseSaying ws : wiseSayings){
            ws.printWiseSaying();
        }
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
}
