package it.disco.unimib.service;

import it.disco.unimib.model.Event;
import it.disco.unimib.repository.EventRepository;
import it.disco.unimib.repository.FileRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class StoreEvents {

    private final FileRepository fileRepository;
    private final EventRepository eventRepo;

    public StoreEvents(@Nullable FileRepository fileRepository, @Nullable EventRepository eventRepo) {
        this.fileRepository = fileRepository;
        this.eventRepo = eventRepo;
    }

    public void Store(Iterable<Event> events) throws IOException {
        if (eventRepo != null) eventRepo.saveAll(events);
        if (fileRepository != null) fileRepository.saveAll(events);
    }

}

