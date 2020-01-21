package it.disco.unimib.configuration;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

//@Profile("ARANGO")
@Data
@Configuration
@Log
@NoArgsConstructor
@ConditionalOnProperty(name = "outputOnArango", havingValue = "enabled")
public class ArangoConf {
    @Value("${spring.data.arangodb.hosts:''}")
    private String host;


    @Value("${spring.data.arangodb.database:''}")
    private String database;

    @PostConstruct
    public void printInfo() {
        log.info("ArangoDB host: " + host);
        log.info("ArangoDB database: " + database);
    }


}
