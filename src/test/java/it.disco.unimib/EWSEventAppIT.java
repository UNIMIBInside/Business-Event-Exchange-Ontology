package it.disco.unimib;


import it.disco.unimib.repository.EventRepository;
import lombok.extern.java.Log;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest(
        properties = {"test_mode=true", "outputOnArango=enable", "outputOnFile=disable",
                "spring.data.arangodb.hosts=localhost:8529",
                "spring.data.arangodb.database=events",
                "spring.data.arangodb.user=root",
                "spring.data.arangodb.password=openSesame", "delete_old_events=true",
                "API=https://virtserver.swaggerhub.com/EW-Shopp/EW-Shopp_Event_API/2.2.0/event/", "starting_date=2018-01-01",
                "ending_date=2020-01-01"})
@Log
public class EWSEventAppIT {

    @ClassRule
    public static DockerComposeContainer compose =
            new DockerComposeContainer(
                    new File("src/test/docker/docker-compose.yml")).withLocalCompose(true);
    @Autowired
    private EventRepository eventRepository;

    @Test
    public void shouldGenerateResultFiles() throws Exception {
        log.info("number of events: " + eventRepository.count());


    }


}