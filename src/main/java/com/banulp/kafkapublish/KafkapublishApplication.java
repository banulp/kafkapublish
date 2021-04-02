package com.banulp.kafkapublish;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class KafkapublishApplication {

    @Autowired
    private Dgweb dgweb;

    @Autowired
    private DgwebJSoup dgwebJSoup;

    @Autowired
    private DgwebCmd dgwebCmd;

    public static void main(String[] args) {
        SpringApplication.run(KafkapublishApplication.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            System.out.println("[start]");
            List<String> argList = Arrays.asList(args.getSourceArgs());

            System.out.println(argList.get(0));
            System.out.println(argList.get(1));

//            dgweb.poll(argList.get(0));
//            dgwebJSoup.poll(argList.get(0), argList.get(1));
            dgwebCmd.poll(argList.get(0));
        };
    }
}