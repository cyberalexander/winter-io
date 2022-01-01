package com.leonovich.winter.io;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@SpringBootApplication
public class WinterIoApplication {

    public static void main(final String... args) {
        SpringApplication.run(WinterIoApplication.class, args);
        log.info("{} started!", WinterIoApplication.class.getSimpleName());
    }
}
