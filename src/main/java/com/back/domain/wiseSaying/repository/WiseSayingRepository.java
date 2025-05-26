package com.back.domain.wiseSaying.repository;

import com.back.domain.wiseSaying.entity.WiseSaying;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WiseSayingRepository {
    private final String basePath = "./db/wiseSaying/";

    public List<WiseSaying> loadAll(){
        List<WiseSaying> list = new ArrayList<>();
        File path = new File(basePath);
        File[] files = path.listFiles((dir, name) -> name.endsWith(".json") && !name.equals("lastId.txt") && !name.equals("build.json"));

        if (files != null){
            for(File file : files){
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = br.readLine()) != null){
                        sb.append(line);
                    }

                    String json = sb.toString();
                    list.add(WiseSaying.fromJson(json));
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public WiseSaying loadById(int id){
        File file = new File(basePath + id + ".json");
        WiseSaying check = new WiseSaying(-1, "", "");
        if(file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                String json = sb.toString();
                return WiseSaying.fromJson(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return check;
    }

    public void saveBuild(){
        String buildPath = basePath + "build.json";
        List<WiseSaying> wiseSayings = loadAll();
        wiseSayings.sort((a, b) -> Integer.compare(a.id, b.id));

        StringBuilder sb = new StringBuilder();
        sb.append("[\n");

        for(int i = 0; i < wiseSayings.size(); i++){
            WiseSaying ws = wiseSayings.get(i);
            sb.append(ws.toBuildJson());

            if(i != wiseSayings.size() -1){
                sb.append(",\n");
            }
        }
        sb.append("\n]");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(buildPath))){
            writer.write(sb.toString());
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void saveFile(WiseSaying wiseSaying){
        try {
            File file = new File(basePath + wiseSaying.id + ".json");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(wiseSaying.toJson());
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean delete(int id){
        File file = new File(basePath + id + ".json");
        if(file.exists()){
            file.delete();
            return true;
        }
        return false;
    }

    public int loadLastId(){
        File file = new File(basePath + "lastId.txt");
        if (!file.exists()) return 1;
        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            return Integer.parseInt(br.readLine().trim());
        } catch (Exception e){
            e.printStackTrace();
            return 1;
        }
    }

    public void saveLastId(int id){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(basePath + "lastId.txt"))){
            writer.write(String.valueOf(id));
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
