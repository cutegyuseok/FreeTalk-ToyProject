package com.cutegyuseok.freetalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FreeTalkApplication {

    public static void main(String[] args) {
        System.setProperty("user.timezone", "Asia/Seoul");
        SpringApplication.run(FreeTalkApplication.class, args);
    }

}
