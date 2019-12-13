package it.disco.unimib;

import com.arangodb.springframework.boot.autoconfigure.ArangoAutoConfiguration;
import it.disco.unimib.model.EventsArray;
import it.disco.unimib.service.StoreEvents;
import lombok.extern.java.Log;
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

@SpringBootApplication(exclude = ArangoAutoConfiguration.class)
@Configuration
@Log
public class EWSEventApp implements CommandLineRunner {

    final StoreEvents storeEvents;

    RestTemplate restTemplate = new RestTemplate();

    @Value("${API}")
    private String EventEndpointAPI;

    @Value("${num_days:0}")
    private int NumDays;

    @Value("${starting_date:null}")
    private String startingDate;

    @Value("${ending_date:null}")
    private String endingDate;

    @Value("${spring.data.arangodb.hosts:''}")
    private String host;

    @Value("${spring.data.arangodb.database:''}")
    private String database;

    @Value("${working_path:~}")
    private String pathName;
    @Value("${fileName:output.txt}")
    private String fileName;
    @Value("${results_dir:results}")
    private String folderName;

    @Value("${spring.autoconfigure.exclude:null}")
    private String exclude;


    public EWSEventApp(StoreEvents storeEvents) {
        this.storeEvents = storeEvents;
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

    @Override
    public void run(String... args) throws Exception {

        if (!exclude.equalsIgnoreCase("null")) {
            log.info("ArangoDB host: " + host);
            log.info("ArangoDB database: " + database);
        }
        processDateProperties();
        log.info("Starting date: " + startingDate);
        log.info("Ending date: " + endingDate);
        log.info("Number of days: " + NumDays);
        log.info("Path name: " + pathName);
        log.info("Folder name " + folderName);
        log.info("File name: " + fileName);

        String url = EventEndpointAPI + startingDate;
        if (NumDays >= 0) url += "P" + NumDays;

        log.info("Endpoint: " + url);

        EventsArray events = retrieveRemoteCustomEvents(url);
        storeEvents.Store(events.getEventArray());

        log.info("added " + events.getEventArray().size() + " events.");


        System.exit(0);

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

}
