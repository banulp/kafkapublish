package com.banulp.kafkapublish;

import com.fasterxml.jackson.databind.JsonNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Service;

@Service
public class DgwebJSoup {

    private final int PAGE_GAP = 5;

    private int fromIdx = 0;

    private int emptyPageCnt = 0;

    @Autowired
    public ConfigurableEnvironment env;

    @Autowired
    private KafkaSendMessage ksm;

    public void poll(String i, String postfix) {
        int index = Integer.valueOf(i);

        while (true) {
            index += 1;
//            System.out.println("before sendPublishMessage : " + emptyPageCnt);

            if (index % 100 == 0) {
                System.out.println("index print of " + postfix + " : " + index);
            }

//            sendPublishMessage(index);
            sendPublishMessage(Integer.valueOf(String.valueOf(index) + String.valueOf(postfix)));
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

        try {
            Document doc = Jsoup.connect("https://www.daangn.com/articles/" + index).get();

            // 성공하면 다시 0
            if (emptyPageCnt != 0) {
//                System.out.println("성공하면 다시 0");
                emptyPageCnt = 0;
            }

            if (doc.getElementById("article-price-nanum") != null && doc.getElementById("region-name").text().contains("분당구")) {
                String msg = String.format("{\"id\":\"%s\",\"title\":\"%s\",\"region\":\"%s\"}", index, doc.getElementById("article-title").text(), doc.getElementById("region-name").text());
                System.out.println(msg);
                // publish
                ksm.sendMessage(msg);
            }

        } catch (Exception e) {
            emptyPageCnt += 1;
//            System.out.println("emptyPageCnt : " + emptyPageCnt);
//            e.printStackTrace();
        }

    }

}
