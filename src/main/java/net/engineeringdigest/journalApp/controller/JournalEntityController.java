package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntityController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("/{userName}")
    public ResponseEntity<?> getJournalEntriesByUser(@PathVariable String userName) {
        //return journalEntryService.getAll();
        User user = userService.findByUserName(userName);
        List<JournalEntry> journalEntries = user.getJournalEntries();
        if(journalEntries != null && !journalEntries.isEmpty()) {
            return new ResponseEntity<>(journalEntries, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{userName}")
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry journalEntry, @PathVariable String userName) {
        try {
            journalEntryService.saveEntry(journalEntry, userName);
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable String myId) {
        Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
        //return Optional.ofNullable(journalEntryService.findById(myId).orElse(null));
        if(journalEntry.isPresent()) {
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{userName}/{myId}")
    public ResponseEntity<?> deleteJournalEntry(@PathVariable String myId, @PathVariable String userName) {
        journalEntryService.deleteById(myId, userName);
        return new ResponseEntity<>(HttpStatus.GONE);
    }

    @PutMapping("id/{userName}/{id}")
    public ResponseEntity<?> updateJournalEntryById(
            @PathVariable String id,
            @RequestBody JournalEntry journalEntry,
            @PathVariable String userName) {
        //journalEntry.setLocalDate(LocalDate.now());

        JournalEntry oldJournalEntry = journalEntryService.findById(id).orElse(null);
        if(oldJournalEntry != null) {
            String title = journalEntry.getTitle();
            if(title != null && !title.equals("")) {
                oldJournalEntry.setTitle(title);
            }
            String content = journalEntry.getContent();
            if(content != null && !content.equals("")) {
                oldJournalEntry.setContent(content);
            }

            journalEntryService.saveEntry(oldJournalEntry);
            return new ResponseEntity<>(oldJournalEntry, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
