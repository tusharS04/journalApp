package net.egnineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.service.UserDetailServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

//MOCKITO EXAMPLE
//@SpringBootTest
public class UserDetailServiceImplTest {

    //@Autowired
    @InjectMocks //
    private UserDetailServiceImpl userDetailService;

    @Mock //use @InjectMocks - which does not loads entire application context
    //@MockBean :use @SpringBootTest - which loads application context
    private UserRepository userRepository;

    @BeforeEach
    //userRepository comes null as application context is not loaded
    //so create userRepository
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void laodUserNameTest() {
        when(userRepository.findByUserName(ArgumentMatchers.anyString()))
                .thenReturn(User.builder().userName("ram").password("slkfjsalk").roles(new ArrayList<>()).build());
        UserDetails userDetails = userDetailService.loadUserByUsername("ram");
        Assertions.assertNotNull(userDetails);
    }
}
