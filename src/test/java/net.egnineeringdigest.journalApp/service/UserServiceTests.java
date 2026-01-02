package net.egnineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.JournalApplication;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = JournalApplication.class)
public class UserServiceTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    //@BeforeEach // run something before each test case
    //@BeforeAll //run something before all test case run
    //similarly @AferAll and @AfterEach

    @Disabled
    @Test
    public void findByUserName() {
        assertEquals(4, 2+2);
        assertNotNull(userRepository.findByUserName("tushar"));
        User user = userRepository.findByUserName("tushar");
        assertFalse(user.getJournalEntries().isEmpty());
    }

    @Disabled
    @ParameterizedTest
    @ValueSource(strings = {
            "ram",
            "sham",
            "vipul"
    })
    public void findByUserName(String name) {
        assertNotNull(userRepository.findByUserName(name), "Failed for " + name);
    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    public void testsSaveNewUser(User user) {
        assertTrue(userService.saveNewUser(user), "Failed for " + user.getUserName());
    }

    @Disabled
    @ParameterizedTest
    @CsvSource({
            "1,1,2",
            "2,10,12",
            "3,3,9"
    })
    public void testAdd(int a, int b, int expected) {
        assertEquals(expected, a+b);
    }
}
