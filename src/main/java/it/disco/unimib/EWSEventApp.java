package it.disco.unimib;

import it.disco.unimib.model.EventsArray;
import it.disco.unimib.service.EventManager;
import lombok.extern.java.Log;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
@Configuration
@Log
public class EWSEventApp implements CommandLineRunner {

    final EventManager eventManager;

    RestTemplate restTemplate = new RestTemplate();

    @Value("${API}")
    private String EventEndpointAPI;

    @Value("${num_days:0}")
    private int NumDays;

    @Value("${starting_date:null}")
    private String startingDate;

    @Value("${ending_date:null}")
    private String endingDate;

    @Value("${working_mode:slow}") // two possible working modes slow and fast
    private String mode;

    @Value("${test_mode:false}")
    private Boolean testMode;

    @Value("${delete_old_events:false}")
    private Boolean deleteOldEvents;

    public EWSEventApp(EventManager eventManager) {
        this.eventManager = eventManager;
    }


    public static void main(String[] args) throws Exception {

        SpringApplication app = new SpringApplication(EWSEventApp.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);

    }

    public static boolean isValidFormat(String value, DateTimeFormatter formatter) {
        LocalDate date;
        try {
            date = LocalDate.parse(value, formatter);
        } catch (DateTimeException e) {
            e.printStackTrace();
            date = null;
        }
        return date != null;
    }

    private static String addDaysJodaTime(String date, int daysToAdd) {
        DateTime dateTime = new DateTime(date);
        return dateTime
                .plusDays(daysToAdd)
                .toString("yyyy-MM-dd");
    }

    private EventsArray retrieveRemoteCustomEvents(String url) {
        // to support responses not only in application/json content type
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);

        ResponseEntity<EventsArray> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<EventsArray>() {
        });
        EventsArray events = response.getBody();
        log.info(response.getStatusCode().toString());
        return events;
    }

    private void processDateProperties() throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        if (startingDate.equalsIgnoreCase("null")) {
            startingDate = formatter.format(OffsetDateTime.now());
        } else if (!isValidFormat(startingDate, formatter)) {
            log.severe("Starting date - invalid format");
            System.exit(1);
        }

        if (!endingDate.equalsIgnoreCase("null")) {
            if (!isValidFormat(endingDate, formatter)) {
                log.severe("Ending date - invalid format");
                System.exit(1);
            }
            LocalDate end = LocalDate.parse(endingDate, formatter);
            LocalDate start = LocalDate.parse(startingDate, formatter);

            final long days = ChronoUnit.DAYS.between(start, end);
            if (days >= 0) NumDays = (int) days;
        }
    }

    @Override
    public void run(String... args) throws Exception {


        processDateProperties();
        log.info("Starting date: " + startingDate);
        log.info("Ending date: " + endingDate);
        log.info("Number of days: " + NumDays);

        if (deleteOldEvents) eventManager.deleteAll();

        if (mode.equalsIgnoreCase("fast")) {
            String url = EventEndpointAPI + startingDate;
            if (NumDays >= 0) url += "P" + NumDays;
            log.info("Endpoint: " + url);

            EventsArray events = retrieveRemoteCustomEvents(url);
            eventManager.store(events.getEventArray());

            log.info("added " + events.getEventArray().size() + " events.");
        } else if (mode.equalsIgnoreCase("slow")) {
            String url = "";
            int tot_events = 0;
            for (int i = 0; i < NumDays; i++) {
                url = EventEndpointAPI + addDaysJodaTime(startingDate, i);
                EventsArray events = retrieveRemoteCustomEvents(url);
                eventManager.store(events.getEventArray());
                tot_events += events.getEventArray().size();
            }
            log.info("downloaded events form " + NumDays + " days");
            log.info("added " + tot_events + " events.");
        } else log.severe("No event could be retrieved because of a wrong working mode");

        if (!testMode) System.exit(0);

    }

}
