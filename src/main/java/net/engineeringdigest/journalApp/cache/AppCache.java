package net.engineeringdigest.journalApp.cache;

import net.engineeringdigest.journalApp.entity.ConfigJournalAppEntity;
import net.engineeringdigest.journalApp.repository.ConfigJournalAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

@Component
public class AppCache {

    @Autowired
    private ConfigJournalAppRepository configJournalAppRepository;

    public enum keys {
        WEATHER_API,
        WEATHER_POST_API;
    }

    public static HashMap<String, String> appCache;

    @PostConstruct
    public void init() {
        appCache = new HashMap<>();
        List<ConfigJournalAppEntity> appEntities = configJournalAppRepository.findAll();
        for(ConfigJournalAppEntity entity : appEntities) {
            appCache.put(entity.getKey(), entity.getValue());
        }
    }
}
