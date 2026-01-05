package net.engineeringdigest.journalApp.controller;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.cache.AppCache;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AppCache appCache;

    //Get All User
    @GetMapping("get-all-user")
    public ResponseEntity<?> getAll() {
        //return journalEntryService.getAll();
        List<User> userEntries = userService.getAll();
        if(userEntries != null && !userEntries.isEmpty()) {
            log.info("{}, users found", userEntries.size());
            return new ResponseEntity<>(userEntries, HttpStatus.OK);
        }
        log.info("No user found");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("create-admin")
    public ResponseEntity<?> createAdmin(@RequestBody User user) {
        try {
            userService.createAdmin(user);
            log.info("New admin created with user name: {}", user.getUserName());
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error while creating admin user : {}", user.getUserName());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("clear-app-cache")
    public void clearCache() {
        appCache.init();
    }
}
