package net.engineeringdigest.journalApp.scheduler;

import net.engineeringdigest.journalApp.cache.AppCache;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.entity.enums.Sentiment;
import net.engineeringdigest.journalApp.repository.UserRepositoryImpl;
import net.engineeringdigest.journalApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AppCache appCache;

    @Scheduled(cron = "0 * * ? * *")
    public void fetchUsersAndSendSAMail() {
        List<User> userList = userRepository.getUserForSA();
        for(User user : userList) {
            List<JournalEntry> journalEntries = user.getJournalEntries();
            //List<String> filteredEntries = journalEntries.stream().filter(x ->
            //x.getDate().isAfter(LocalDate.now().minus(7, ChronoUnit.DAYS)))
            //.map(x -> x.getContent()).collect(Collectors.toList());

            List<Sentiment> sentiments = journalEntries.stream().filter(x ->
                            x.getDate().isAfter(LocalDate.now().minus(7, ChronoUnit.DAYS)))
                    .map(x -> x.getSentiment()).collect(Collectors.toList());

            HashMap<Sentiment, Integer> sentimentCountMap = new HashMap<>();
            for(Sentiment sentiment : sentiments) {
                if(sentiment != null) {
                    sentimentCountMap.put(sentiment, sentimentCountMap.getOrDefault(sentiment,0)+1);
                }
            }

            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;
            for(Map.Entry<Sentiment, Integer> entry : sentimentCountMap.entrySet()) {
                if(entry.getValue() > maxCount) {
                    maxCount += entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }

            //String entry = String.join(" ", filteredEntries);
            if(mostFrequentSentiment != null)
            emailService.sendEmail(user.getEmail(), "Sentiment Analysis for last 7 Days", "Happy");
        }
    }

    @Scheduled(cron = "0 0/1 * ? * *")
    public void clearAppCache() {
        appCache.init();
    }
}
