package com.back;

import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.standard.util.TestUtil;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {
    @Test
    public static String run(String input) {
        InputStream originalIn = System.in;
        OutputStream originalOut = System.out;

        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        PrintStream testPrintOut = new PrintStream(testOut);

        System.setIn(testIn);
        System.setOut(testPrintOut);

        try {
            App app = new App();
            app.run();
            testPrintOut.flush();

            return testOut.toString();

        } finally {
            System.setIn(originalIn);
            System.setOut((PrintStream) originalOut);

            try {
                testIn.close();
                testOut.close();
                testPrintOut.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }


    }

    @Test
    public static void clear(){
        String basePath = "./db/wiseSaying/";

        File path = new File(basePath);
        File[] files = path.listFiles((dir, name) -> name.endsWith(".json") || name.endsWith(".txt"));

        if (files != null){
            for(File file : files){
                file.delete();
            }
        }
    }
}
