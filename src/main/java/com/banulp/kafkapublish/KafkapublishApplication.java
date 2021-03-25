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
    private Dgweb dgweb;

    public static void main(String[] args) {
        SpringApplication.run(KafkapublishApplication.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            System.out.println("[start]");
            Arrays.asList(args.getSourceArgs()).forEach(a -> {
                System.out.println(a);
                dgweb.poll(a);
//                ksm.sendMessage(a);
            });

        };
    }
}