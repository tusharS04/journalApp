package net.engineeringdigest.journalApp.controller;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.api.reponse.WeatherResponse;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import net.engineeringdigest.journalApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private WeatherService weatherService;

    @PutMapping()

    //TODO:  Authenticate user before coming to this
    // @PathVariable String userName
    // Do not take username from path , authenticate user first

    public ResponseEntity<?> updateUser(@RequestBody User user) {
        // Authenticate user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User userInDB = userService.findByUserName(userName);
       // if(userInDB != null) {
            userInDB.setUserName(user.getUserName());
            userInDB.setPassword(user.getPassword());
            userService.saveNewUser(userInDB);
            //return new ResponseEntity<>(userInDB, HttpStatus.OK);
        //}
        log.info("User updated successfully : {}", userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser() {
        // Authenticate user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        userRepository.deleteByUserName(userName);
        log.info("user deleted successfully : {}", userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<?> greeting() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        String city = "Mumbai";
        WeatherResponse weatherResponse = weatherService.getWeather(city);
        String greetings = "";
        if(weatherResponse != null) {
            greetings = "Hi " + userName + " ,The current weather in " +city+ " is : " + weatherResponse.getCurrent().getTempC();
        }
        return new ResponseEntity<>(greetings, HttpStatus.OK);
    }

    @GetMapping("/bulk")
    public ResponseEntity<?> greetingByPost() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        String weatherResponse = weatherService.getBulkWeather();
        String greetings = "";
        if(weatherResponse != null) {
            greetings = "Hi " + userName + " ,The post response from weather API looks like this : " + weatherResponse;
        }
        return new ResponseEntity<>(greetings, HttpStatus.OK);
    }
}
