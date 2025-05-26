package com.back.domain.wiseSaying.entity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WiseSaying {
    public int id;
    public String content;
    public String author;

    public WiseSaying(int id, String content, String author){
        this.id = id;
        this.content = content;
        this.author = author;
    }

    public String toJson() {
        return "{\n" +
                "  \"id\": " + this.id + ",\n" +
                "  \"content\": \"" + this.content + "\",\n" +
                "  \"author\": \"" + this.author + "\"\n" +
                "}";
    }

    public String toBuildJson() {
        return "  {\n" +
                "    \"id\": " + this.id + ",\n" +
                "    \"content\": \"" + this.content + "\",\n" +
                "    \"author\": \"" + this.author + "\"\n" +
                "  }";
    }

    public static WiseSaying fromJson(String json){
        WiseSaying wiseSaying = new WiseSaying(-1, "", "");

        Pattern idPattern = Pattern.compile("\"id\"\\s*:\\s*(\\d+)");
        Pattern contentPattern = Pattern.compile("\"content\"\\s*:\\s*\"(.*?)\"");
        Pattern authorPattern = Pattern.compile("\"author\"\\s*:\\s*\"(.*?)\"");

        Matcher idMatcher = idPattern.matcher(json);
        Matcher contentMatcher = contentPattern.matcher(json);
        Matcher authorMatcher = authorPattern.matcher(json);


        if (idMatcher.find()) {
            wiseSaying.id = Integer.parseInt(idMatcher.group(1));
        }
        if (contentMatcher.find()) {
            wiseSaying.content = contentMatcher.group(1);
        }
        if (authorMatcher.find()) {
            wiseSaying.author = authorMatcher.group(1);
        }

        return wiseSaying;
    }

    public void printWiseSaying() {
        System.out.println(this.id + " / " + this.author + " / " + this.content);
    }
}
