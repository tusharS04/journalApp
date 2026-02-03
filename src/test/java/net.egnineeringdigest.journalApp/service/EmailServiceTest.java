package net.egnineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    void sendTestMail() {
        emailService.sendEmail(
                "tusharsomvanshi0406@gmail.com",
                "Test Mail",
                "Hello ! How are you ?"
        );
    }
}
