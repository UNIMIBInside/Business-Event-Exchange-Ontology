package it.disco.unimib.configuration;


import com.arangodb.springframework.boot.autoconfigure.ArangoAutoConfiguration;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Data
@Configuration
//@Profile("FILE")
@EnableAutoConfiguration(exclude = ArangoAutoConfiguration.class)
@Log
@NoArgsConstructor
@ConditionalOnProperty(name = "outputOnFile", havingValue = "enabled")
public class FileConf {

    @Value("${working_path:~}")
    private String pathName;
    @Value("${fileName:output.txt}")
    private String fileName;
    @Value("${results_dir:results}")
    private String folderName;

    @PostConstruct
    public void printInfo() {
        log.info("Path name: " + pathName);
        log.info("Folder name " + folderName);
        log.info("File name: " + fileName);
    }

}
