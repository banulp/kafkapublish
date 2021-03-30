package com.banulp.kafkapublish;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class DgwebCmd {

    private final int PAGE_GAP = 5;

    private int fromIdx = 0;

    private int emptyPageCnt = 0;

    @Autowired
    public ConfigurableEnvironment env;

    @Autowired
    private KafkaSendMessage ksm;

    public void poll(String i) {
        int index = Integer.valueOf(i);

        while (true) {
            index += 1;
//            System.out.println("before sendPublishMessage : " + emptyPageCnt);
            sendPublishMessage(index);
//            System.out.println("after sendPublishMessage : " + emptyPageCnt);
            if (emptyPageCnt > PAGE_GAP) {
                try {
//                    System.out.println("in emptyPageCnt : " + index);
                    Thread.sleep(5000);
                    index -= PAGE_GAP;
                    emptyPageCnt = 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void sendPublishMessage(int i) {
//        System.out.println("get page : " + i);
        if (i % 100 == 0) {
            System.out.println("index print of " + " : " + i);
        }

        String index = String.valueOf(i);
        String title = "title";
        String region = "region";
        boolean go = false;

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", "wget -q -O - https://www.daangn.com/articles/" + index + " | grep '무료나눔'");
        try {
            Process process = processBuilder.start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
//                System.out.println("Success!");
//                System.out.println(output);
                System.exit(0);
            } else {
                //abnormal...
            }

            if(output.toString().length() != 0){
                go = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            URL url = new URL("https://www.daangn.com/articles/" + index);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

            // 성공하면 다시 0
            if (emptyPageCnt != 0) {
//                System.out.println("성공하면 다시 0");
                emptyPageCnt = 0;
            }

            String temp;
            while ((temp = br.readLine()) != null) {
                if (temp.contains("article-price-nanum")) {
                    go = true;
                }
                if (temp.contains("article-title")) {
                    title = temp;
                }
                if (temp.contains("\"region-name")) {
                    if (temp.contains("분당구")) {
                        region = temp;
                    } else {
                        return;
                    }
                }
            }

            if (go) {
                String msg = String.format("{\"id\":\"%s\",\"title\":\"%s\",\"region\":\"%s\"}", index, h2t(title), h2t(region));
//                System.out.println(msg);
                // publish
                ksm.sendMessage(msg);
            }

        } catch (Exception e) {
            emptyPageCnt += 1;
//            System.out.println("emptyPageCnt : " + emptyPageCnt);
//            e.printStackTrace();
        }

    }

    public static String h2t(String html) {
        return Jsoup.parse(html).text();
    }

}
