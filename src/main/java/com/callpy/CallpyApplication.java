package com.callpy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//public class CallpyApplication extends SpringBootServletInitializer {
public class CallpyApplication {

    public static void main(String[] args) {
        SpringApplication.run(CallpyApplication.class, args);
    }

//    @Override
//    protected SpringApplicationBuilder configure(
//            SpringApplicationBuilder builder) {
//        return builder.sources(CallpyApplication.class);
//    }
}
