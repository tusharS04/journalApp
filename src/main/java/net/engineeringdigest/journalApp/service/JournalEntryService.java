package net.engineeringdigest.journalApp.service;

//import com.sun.org.slf4j.internal.LoggerFactory;
import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@Configuration
@Slf4j
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName) {
        try {
            //store journal entry in db
            journalEntry.setDate(LocalDate.now());
            journalEntryRepository.save(journalEntry);
            //add journal entry against user then save user in db with journal
            User user = userService.findByUserName(userName);
            user.getJournalEntries().add(journalEntry);
            userService.saveUser(user);
            log.info("User updated : {}", user.getUserName());
        } catch (Exception e)       {
            log.error("An error occurred while saving entry", e);
            throw new RuntimeException("An error occurred while saving entry", e);
        }
    }

    public void saveEntry(JournalEntry journalEntry) {
        //store journal entry in db
        journalEntry.setDate(LocalDate.now());
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll() {
            return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(String id) {
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteById(String id, String userName) {
        /*User user = userService.findByUserName(userName);
       List<JournalEntry> journalEntries = user.getJournalEntries();
        //this gives ConcurrentModificationException
        for (JournalEntry journalEntry : journalEntries) {
            if (id.equals(journalEntry.getId())) {
                journalEntries.remove(journalEntry);
            }
        }*/
        //User iterator to ConcurrentModification
        /*Iterator<JournalEntry> iterator = journalEntries.iterator();
        while(iterator.hasNext()) {
            JournalEntry journalEntry = iterator.next();
            if(id.equals(journalEntry.getId())) {
                iterator.remove();
            }
        }*/
        boolean isRemoved = false;
        try {
            User user = userService.findByUserName(userName);
            //simple replace the above thing with lambda method
            isRemoved = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if(isRemoved) {
                userService.saveUser(user);
                journalEntryRepository.deleteById(id);
            }
        } catch (Exception e) {
            log.error("An error occured while deleting the entry",e);
            throw new RuntimeException("An error occured while deleting the entry",e);
        }
        return isRemoved;
    }
}
