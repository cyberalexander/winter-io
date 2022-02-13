package com.leonovich.winter.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class WinterIoApplicationTests {

    @Value("${application.some.parameter}")
    private String someParameter;

    @Test
    void contextLoads() {
        //Default unit test verifying if Spring Boot Context initialization successful
    }

    @Test
    void testParameterValueTakenFromTestProperties() {
        Assertions.assertEquals("some test value", someParameter);
    }
}
