package it.disco.unimib.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.disco.unimib.model.Event;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Component
@ConditionalOnProperty(name = "outputOnFile", havingValue = "enabled")
public class FileRepository {

    private String folderName;
    private String fileName;
    private BufferedWriter bufferedWriter;

    @Autowired
    public FileRepository(@Value("${fileName:output.txt}") String fileName,
                          @Value("${folderName:results}") String folderName) throws IOException {
        this.fileName = fileName;
        this.folderName = folderName;
        createOrEmptyFolder();
    }

    public void saveAll(Iterable<Event> events) throws IOException {
        createFile();
        ObjectMapper objectMapper = new ObjectMapper();
        for (Event event : events) {
            String str = null;
            try {
                str = objectMapper.writeValueAsString(event);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            try {
                bufferedWriter.write(str);
                bufferedWriter.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        bufferedWriter.flush();

    }

    private void createOrEmptyFolder() throws IOException {
        File dir = new File(folderName);
        if (!dir.exists()) dir.mkdir();
        else FileUtils.cleanDirectory(dir);
    }

    private void createFile() throws IOException {
        Path path = Paths.get(folderName, fileName);
        File file = new File(path.toUri());

        if (!file.exists()) path = Files.createFile(path);
        bufferedWriter = Files.newBufferedWriter(path);

    }


}
