package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Server health good";
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            userService.saveNewUser(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //Get All User
    @GetMapping("get-all-user")
    public ResponseEntity<?> getAll() {
        //return journalEntryService.getAll();
        List<User> userEntries = userService.getAll();
        if(userEntries != null && !userEntries.isEmpty()) {
            return new ResponseEntity<>(userEntries, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
