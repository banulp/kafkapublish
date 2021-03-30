package com.banulp.kafkapublish;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.BufferedReader;
import java.io.IOException;
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
                if (temp.contains("\"region-name")) {
//                    region = temp;
                    region = temp.substring(temp.indexOf(">") + 1, temp.lastIndexOf("<"));
                }

//                System.out.println(go);
//                System.out.println(index);
//                System.out.println(title);
//                System.out.println(nickname);
//                System.out.println(region);

                String msg = String.format("{\"id\":\"%s\",\"title\":\"%s\",\"region\":\"%s\"}", index, title, region);

                System.out.println(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void contextLoadsJsoup() {

        String index = "213652663";
//        String index = "21365266300";
        try {
            Document doc = Jsoup.connect("https://www.daangn.com/articles/" + index).get();

            Element element = doc.getElementById("article-price-nanum");
            String region = doc.getElementById("region-name").text();
            if (element != null && region.contains("분당구")) {
                String msg = String.format("{\"id\":\"%s\",\"title\":\"%s\",\"region\":\"%s\"}", index, "", "");
                System.out.println(msg);
            }

        } catch (IOException e) {
            System.out.println("EXCEPTION!!");
            e.printStackTrace();
        }

    }


    @Test
    void indexTest() {
        poll("5237", "1");
    }

    public void poll(String i, String postfix) {
        int index = Integer.valueOf(i);
        for( int j = 0 ; j < 20; j++) {
            index += 1;
            System.out.println(Integer.valueOf(String.valueOf(index) + String.valueOf(postfix)));
        }
    }

}
