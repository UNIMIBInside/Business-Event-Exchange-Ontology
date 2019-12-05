package it.disco.unimib;

import it.disco.unimib.model.Event;
import it.disco.unimib.model.EventsArray;
import it.disco.unimib.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
@Configuration
public class EWSEventApp implements CommandLineRunner {
	
	@Autowired
	EventRepository eventRepo;

	@Value("${API}")
	private String API;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(EWSEventApp.class, args);
	}

	
	@Override
	public void run(String... args) throws Exception {
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
		String date = formatter.format(OffsetDateTime.now());
		int N = 0;

		if (args.length != 0) {
			date = args[0];
			if (args.length > 1)
				N = Integer.valueOf(args[1]);
		}

		RestTemplate restTemplate = new RestTemplate();
		String url = API + date;
		if(N!=0)
			url	+= "P" + N;

		try {
			// to support responses not only in application/json content type
			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
			converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
			messageConverters.add(converter);
			restTemplate.setMessageConverters(messageConverters);

			ResponseEntity<EventsArray> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<EventsArray>() {
			});
			EventsArray events = response.getBody();
			for (Event event : events.getEventArray())
				eventRepo.save(event);

			System.out.println(response.getStatusCode());
			System.out.println("added" + events.getEventArray().size() + " events");
		} 
		catch (final HttpClientErrorException e) {
			System.out.println(e.getStatusCode());
			System.out.println(e.getResponseBodyAsString());
		} 
		catch (HttpServerErrorException e) {
			System.out.println(e.getStatusCode());
			System.out.println(e.getResponseBodyAsString());
		}

	}

}
