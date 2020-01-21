package it.disco.unimib.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.disco.unimib.model.Event;
import lombok.SneakyThrows;
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


    private String pathName;
    private String folderName;
    private String fileName;
    private BufferedWriter bufferedWriter;
    private Path resultsFullPath;

    @Autowired
    public FileRepository(@Value("${working_path:~}") String pathName,
                          @Value("${fileName:output.txt}") String fileName,
                          @Value("${results_dir:results}") String folderName) throws IOException {
        this.fileName = fileName;
        this.folderName = folderName;
        this.pathName = pathName;
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

    @SneakyThrows
    public void deleteAll() {
        Path path = Paths.get(resultsFullPath.toString(), fileName);
        File file = new File(path.toUri());
        if (file.exists()) Files.delete(path);
    }

    private void createOrEmptyFolder() throws IOException {
        Path path = Paths.get(pathName, folderName);
        resultsFullPath = path.toAbsolutePath();
        File dir = new File(path.toUri());
        if (!dir.exists()) dir.mkdir();
        else FileUtils.cleanDirectory(dir);
    }

    private void createFile() throws IOException {
        Path path = Paths.get(resultsFullPath.toString(), fileName);
        File file = new File(path.toUri());
        if (!file.exists()) path = Files.createFile(path);
        bufferedWriter = Files.newBufferedWriter(path);

    }


}
