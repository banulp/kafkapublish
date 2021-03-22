package com.banulp.kafkapublish;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class KafkapublishApplication {

    @Autowired
    private KafkaSendMessage ksm;

    public static void main(String[] args) {
        SpringApplication.run(KafkapublishApplication.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            System.out.println("start");

            Arrays.asList(args).forEach(a -> System.out.println(a));

            List<String> list = new ArrayList<String>();
            list.add("{\"id\":\"206800996\",\"title\":\"노트북\",\"region\":\"성남시 분당구 판교동\"}");
            list.add("{\"id\":\"206908580\",\"title\":\"와플 팬\",\"region\":\"성남시 분당구 운중동\"}");
            list.add("{\"id\":\"206721398\",\"title\":\"레고블록\",\"region\":\"성남시 중원구 성남동\"}");
            list.forEach(l -> {
                System.out.println("in");
                        ksm.sendMessage(l);
                    }
            );
//            for (int i = 0; i < 10; i++) {
//                ksm.sendMessage("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
//            }
        };
    }
}