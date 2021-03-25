package com.banulp.kafkapublish;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class Dgweb {

    private int fromIdx = 0;

    @Autowired
    public ConfigurableEnvironment env;

    @Autowired
    private KafkaSendMessage ksm;

    public void poll(int i) throws InterruptedException {

        int index = i;

        while(true) {
            index += 1;
            sendPublishMessage(index);
        }

    }

    public void sendPublishMessage(int i) throws InterruptedException {
//            Thread.sleep(5000);

        String index = String.valueOf(i);
        String title = "title";
        String nickname = "nickname";
        String region = "region";
        boolean go = false;

        try {
            URL url = new URL("https://www.daangn.com/articles/" + index);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String temp;

            while ((temp = br.readLine()) != null) {
                if (temp.contains("article-price-nanum")) {
                    go = true;
                }
                if (temp.contains("article-title")) {
//                    title = temp;
                    title = temp.substring(temp.indexOf(">") + 1, temp.lastIndexOf("<"));
                }
                if (temp.contains("nickname")) {
//                    nickname = temp;
                    nickname = temp.substring(temp.indexOf(">") + 1, temp.lastIndexOf("<"));
                }
                if (temp.contains("\"region-name")) {
//                    region = temp;
                    region = temp.substring(temp.indexOf(">") + 1, temp.lastIndexOf("<"));
                }

                String msg = String.format("{\"id\":\"%s\",\"title\":\"%s\",\"nickname\":\"%s\",\"region\":\"%s\"}", index, title, nickname, region);
//                System.out.println(msg);

                if (go) {
                    System.out.println(msg);
                    // publish
                    ksm.sendMessage(msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
