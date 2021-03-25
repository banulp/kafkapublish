package com.banulp.kafkapublish;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@SpringBootTest
@ActiveProfiles("local")
class KafkapublishApplicationTests {

    @Test
    void contextLoads() {
        // 무료나눔\|article-title\|nickname\|"region-name
        String index = "211853955";
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
                    title = temp.substring(temp.indexOf(">")+1, temp.lastIndexOf("<"));
                }
                if (temp.contains("nickname")) {
//                    nickname = temp;
                    nickname = temp.substring(temp.indexOf(">")+1, temp.lastIndexOf("<"));
                }
                if (temp.contains("\"region-name")) {
//                    region = temp;
                    region = temp.substring(temp.indexOf(">")+1, temp.lastIndexOf("<"));
                }

//                System.out.println(go);
//                System.out.println(index);
//                System.out.println(title);
//                System.out.println(nickname);
//                System.out.println(region);

                String msg = String.format("{\"id\":\"%s\",\"title\":\"%s\",\"nickname\":\"%s\",\"region\":\"%s\"}", index, title, nickname, region);

                System.out.println(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
