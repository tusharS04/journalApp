package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JournalEntryService journalEntryService;

   /* @GetMapping
    public ResponseEntity<?> getAll() {
        //return journalEntryService.getAll();
        List<User> userEntries = userService.getAll();
        if(userEntries != null && !userEntries.isEmpty()) {
            return new ResponseEntity<>(userEntries, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }*/

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
            userService.saveEntry(userInDB);
            //return new ResponseEntity<>(userInDB, HttpStatus.OK);
        //}
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser() {
        // Authenticate user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        userRepository.deleteByUserName(userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
