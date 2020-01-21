package it.disco.unimib.service;

import it.disco.unimib.model.Event;
import it.disco.unimib.repository.EventRepository;
import it.disco.unimib.repository.FileRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.stream.StreamSupport;

@Service
public class EventManager {

    private final FileRepository fileRepository;
    private final EventRepository eventRepo;

    public EventManager(@Nullable FileRepository fileRepository, @Nullable EventRepository eventRepo) {
        this.fileRepository = fileRepository;
        this.eventRepo = eventRepo;
    }

    public void store(Iterable<Event> events) throws IOException {
        if (StreamSupport.stream(events.spliterator(), false).count() > 0) {
            if (eventRepo != null) eventRepo.saveAll(events);
            if (fileRepository != null) fileRepository.saveAll(events);
        }
    }

    public void deleteAll() {
        if (eventRepo != null) eventRepo.deleteAll();
        if (fileRepository != null) fileRepository.deleteAll();

    }

}

