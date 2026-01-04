package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.api.reponse.WeatherResponse;
import org.json.JSONString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class WeatherService {

    private static final String API_KEY = "23cb378a0dcc458aa8a165327260301";
    private static final String API = "https://api.weatherapi.com/v1/current.json?key=API_KEY&q=CITY&aqi=no";
    private static final String POST_API = "http://api.weatherapi.com/v1/current.json?key=API_KEY&q=bulk";

    //RestTemplate process http request and provides response
    @Autowired
    private RestTemplate restTemplate;

    public WeatherResponse getWeather(String city) throws Exception {
        try {
            String finalApi = API.replace("API_KEY", API_KEY).replace("CITY", city);
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
        String finalApi = API.replace("API_KEY", API_KEY);

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
