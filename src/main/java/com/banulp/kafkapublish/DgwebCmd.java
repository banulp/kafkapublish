package com.banulp.kafkapublish;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;

@Service
public class DgwebCmd {

    private final int PAGE_GAP = 30;

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
            sendPublishMessage(index);
//            sendPublishMessage(Integer.valueOf(String.valueOf(index) + String.valueOf(postfix)));
//            System.out.println("after sendPublishMessage : " + emptyPageCnt);
            if (emptyPageCnt > PAGE_GAP) {
                try {
                    System.out.println("in emptyPageCnt : " + index);
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
            System.out.println(DateFormat.getInstance().format(System.currentTimeMillis()) + " - " + i);
        }
        String index = String.valueOf(i);
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", "/home/banulp/toy/dreamcatcher/dreamcather.sh " + index);
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
//                System.exit(0);
            } else {
                //abnormal...
                System.out.println("abnormal exe end");
            }

            // -1, 0, 1
            String result = output.toString();
            System.out.println("result is " + result+ ".");
            if (result.contains("성남시")) {
//                System.out.println("OK action");
                String msg = String.format("{\"id\":\"%s\",\"title\":\"%s\",\"region\":\"%s\"}", index, "", result);
                ksm.sendMessage(msg);
            } else if (result.contains("EMPTY")) {
//                System.out.println("EMPTY action");
                emptyPageCnt += 1;
            } else {
//                System.out.println("NORMAL action");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
