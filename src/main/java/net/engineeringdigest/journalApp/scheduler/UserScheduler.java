package net.engineeringdigest.journalApp.scheduler;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.cache.AppCache;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.entity.enums.Sentiment;
import net.engineeringdigest.journalApp.model.SentimentData;
import net.engineeringdigest.journalApp.repository.UserRepositoryImpl;
import net.engineeringdigest.journalApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserScheduler {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AppCache appCache;

    /**
     * Sending mail via SimpleMailMessage scheduler
     */
    //https://crontab.cronhub.io/
    //@Scheduled(cron = "0 * * ? * *") //every minute
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
            emailService.sendEmail(user.getEmail(), "Sentiment Analysis for last 7 Days", String.valueOf(mostFrequentSentiment));
        }
    }

    @Scheduled(cron = "0 0/1 * ? * *")
    public void clearAppCache() {
        appCache.init();
    }

    /**
     * KAFKA TOPIC
     * Sending mail via kafka topic instead of emailService
     */

    @Autowired
    private KafkaTemplate<String, SentimentData> kafkaTemplate;

    @Scheduled(cron = "0 * * ? * *") //every minute
    public void fetchUsersAndSendSAMailByKafka() {
        List<User> userList = userRepository.getUserForSA();
        for (User user : userList) {
            List<JournalEntry> journalEntries = user.getJournalEntries();
            //List<String> filteredEntries = journalEntries.stream().filter(x ->
            //x.getDate().isAfter(LocalDate.now().minus(7, ChronoUnit.DAYS)))
            //.map(x -> x.getContent()).collect(Collectors.toList());

            List<Sentiment> sentiments = journalEntries.stream().filter(x ->
                            x.getDate().isAfter(LocalDate.now().minus(50, ChronoUnit.DAYS)))
                    .map(x -> x.getSentiment()).collect(Collectors.toList());

            HashMap<Sentiment, Integer> sentimentCountMap = new HashMap<>();
            for (Sentiment sentiment : sentiments) {
                if (sentiment != null) {
                    sentimentCountMap.put(sentiment, sentimentCountMap.getOrDefault(sentiment, 0) + 1);
                }
            }

            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;
            for (Map.Entry<Sentiment, Integer> entry : sentimentCountMap.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount += entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }

            //String entry = String.join(" ", filteredEntries);
            if (mostFrequentSentiment != null) {
                SentimentData sentimentData = SentimentData.builder()
                        .email(user.getEmail()).sentiment("Sentiment Analysis for last 7 Days " + mostFrequentSentiment)
                        .build();
                try {
                    kafkaTemplate.send("weekly-sentiments", user.getEmail(), sentimentData);
                } catch (Exception e) {
                    log.info("error sending mail via kafka consumer... sending direct email by email service");
                    emailService.sendEmail(user.getEmail(), "Sentiment Analysis for last 7 Days " , String.valueOf(mostFrequentSentiment));
                }
                //sendEmail(user.getEmail(), "Sentiment Analysis for last 7 Days", "Happy");
            }
        }
    }

}
