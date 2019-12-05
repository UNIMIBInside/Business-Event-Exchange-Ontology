package it.disco.unimib.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import it.disco.unimib.model.Event;

public interface EventRepository extends ArangoRepository<Event, String> {

}