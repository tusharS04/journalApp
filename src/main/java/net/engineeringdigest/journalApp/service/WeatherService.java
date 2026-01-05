package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.Constants.PlaceHolders;
import net.engineeringdigest.journalApp.api.reponse.WeatherResponse;
import net.engineeringdigest.journalApp.cache.AppCache;
import org.json.JSONString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static net.engineeringdigest.journalApp.cache.AppCache.keys.WEATHER_API;
import static net.engineeringdigest.journalApp.cache.AppCache.keys.WEATHER_POST_API;

@Service
@Slf4j
public class WeatherService {

    @Value("${weather.api.key}")
    private String API_KEY;
    //private static final String API = "https://api.weatherapi.com/v1/current.json?key=API_KEY&q=CITY&aqi=no";
    //private static final String POST_API = "http://api.weatherapi.com/v1/current.json?key=API_KEY&q=bulk";

    //RestTemplate process http request and provides response
    @Autowired
    private RestTemplate restTemplate;

    public WeatherResponse getWeather(String city) throws Exception {
        try {
            String weatherApi = AppCache.appCache.get(WEATHER_API.toString());
            String finalApi = weatherApi.replace(PlaceHolders.API_KEY, API_KEY).replace(PlaceHolders.CITY, city);
            //Deserializing WeatherResponse -> converting JSON response to corresponding Java Object
            ResponseEntity<WeatherResponse> weatherResponse = restTemplate.exchange(finalApi, HttpMethod.GET, null, WeatherResponse.class);
            log.info("Weather API Status code: {}", weatherResponse.getStatusCode());
            return weatherResponse.getBody();
        } catch (Exception e) {
            log.error("Exception :", e);
            throw new Exception(e);
        }
    }

    public String getBulkWeather() {
        String weatherApi = AppCache.appCache.get(WEATHER_POST_API.toString());
        String finalApi = weatherApi.replace(PlaceHolders.API_KEY, API_KEY);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        String data = "{\"locations\":[{\"q\":\"53,-0.12\",\"custom_id\":\"my-id-1\"},{\"q\":\"London\"," +
                "\"custom_id\":\"any-internal-id\"},{\"q\":\"90201\",\"custom_id\":\"us-zipcode-id-765\"}]}";

        HttpEntity<String> httpEntity = new HttpEntity<>(data,httpHeaders);
        ResponseEntity<String> weatherResponse = restTemplate.exchange(finalApi, HttpMethod.POST, httpEntity, String.class);
        log.info("Weather API Status code: {}", weatherResponse.getStatusCode());
        return weatherResponse.getBody();
    }
}
