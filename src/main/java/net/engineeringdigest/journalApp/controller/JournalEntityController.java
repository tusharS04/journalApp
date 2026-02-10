package net.engineeringdigest.journalApp.controller;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.entity.enums.Sentiment;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/journal")
@Slf4j
public class JournalEntityController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<?> getJournalEntriesByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        //return journalEntryService.getAll();
        User user = userService.findByUserName(userName);
        List<JournalEntry> journalEntries = user.getJournalEntries();
        if(journalEntries != null && !journalEntries.isEmpty()) {
            log.info("{}, journal entries found", journalEntries.size());
            return new ResponseEntity<>(journalEntries, HttpStatus.OK);
        }
        log.info("No journal entries found for the user : {}", user.getUserName());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry journalEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        try {
            journalEntryService.saveEntry(journalEntry, userName);
            log.info("Journal entry created by id: {}" , journalEntry.getId());
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Exception occurred "+ e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable String myId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> entries = user.getJournalEntries();
        for(JournalEntry journal : entries) {
            if(journal.getId().equals(myId)) {
                log.info("journal found");
                return new ResponseEntity<>(journal, HttpStatus.OK);
            }
        }
        log.info("no journal found");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteJournalEntry(@PathVariable String myId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean isRemoved = journalEntryService.deleteById(myId, userName);
        return isRemoved ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("id/{id}")
    public ResponseEntity<?> updateJournalEntryById(@PathVariable String id, @RequestBody JournalEntry journalEntry) {
        //journalEntry.setLocalDate(LocalDate.now());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> entries = user.getJournalEntries();
        for(JournalEntry oldJournalEntry : entries) {
            if(oldJournalEntry.getId().equals(id)) {
                //update this oldJournalEntry
                String title = journalEntry.getTitle();
                if(title != null && !title.equals("")) {
                    oldJournalEntry.setTitle(title);
                }
                String content = journalEntry.getContent();
                if(content != null && !content.equals("")) {
                    oldJournalEntry.setContent(content);
                }
                Sentiment sentiment = journalEntry.getSentiment();
                if(sentiment != null && !sentiment.equals("")) {
                    oldJournalEntry.setSentiment(sentiment);
                }
                journalEntryService.saveEntry(oldJournalEntry);
                return new ResponseEntity<>(oldJournalEntry, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
