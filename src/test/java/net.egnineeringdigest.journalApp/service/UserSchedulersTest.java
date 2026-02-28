package net.egnineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.JournalApplication;
import net.engineeringdigest.journalApp.scheduler.UserScheduler;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = JournalApplication.class)
public class UserSchedulersTest {

    @Autowired
    private UserScheduler userScheduler;


    @Disabled
    @Test
    public void fetchUserAndSendSAMail() {
        userScheduler.fetchUsersAndSendSAMail();
    }

    @Disabled
    @Test
    public void fetchUserAndSendSAMailKafka() {
        userScheduler.fetchUsersAndSendSAMailByKafka();
    }
}
