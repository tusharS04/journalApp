package net.egnineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;

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

    @Test
    void sendMailWithAttachment() throws MessagingException {
        emailService.sendMailWithAttachment(
                "tusharsomvanshi0406@gmail.com",
                "Email with attachment and HTML",
                "<h3>Your Attachment</h3><p>Please find the attached document.</p>",
                "C:\\Users\\Tushar\\Downloads\\invoice_PO10000880756-723.pdf",
                "Attachment.pdf"
        );
    }
}
