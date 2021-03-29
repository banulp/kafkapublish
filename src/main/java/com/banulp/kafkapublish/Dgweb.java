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
public class Dgweb {

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
        String index = String.valueOf(i);
        String title = "title";
        String region = "region";
        boolean go = false;

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
                if (temp.contains("article-price")) {
                    return;
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
