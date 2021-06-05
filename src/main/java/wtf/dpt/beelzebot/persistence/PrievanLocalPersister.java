package wtf.dpt.beelzebot.persistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import wtf.dpt.beelzebot.model.PrievanEventDTO;

import javax.annotation.PostConstruct;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrievanLocalPersister extends LocalPersister<List<PrievanEventDTO>> {

    public static final String FILE_PATH = "/persistence/";
    public final static String FILE_NAME = "prievan.bpf";

    @Override
    public boolean save(List<PrievanEventDTO> savedObject) {

        String originalFilePath = String.format("%s%s", FILE_PATH, FILE_NAME);
        String tempFilePath = String.format("%s.temp", originalFilePath);
        File originalFile = new File(originalFilePath);
        File tempFile = new File(tempFilePath);

        try (FileWriter fw = new FileWriter(tempFile)) {

            Gson gson = new Gson();
            String jsonString = gson.toJson(savedObject);
            fw.write(jsonString);

            Files.move(tempFile.toPath(), originalFile.toPath());

        } catch (IOException ignored) {
            //todo: handle
            return false;
        }

        return true;
    }

    @Override
    public List<PrievanEventDTO> load() {

        String filePath = String.format("%s%s", FILE_PATH, FILE_NAME);
        File file = new File(filePath);

        String jsonString = "{}";
        try (FileInputStream fis = new FileInputStream(file)) {

            byte[] bytes = new byte[(int) file.length()];
            fis.read(bytes);
            jsonString = new String(bytes, StandardCharsets.UTF_8);

        } catch (IOException e) {
            //todo: handle
        }

        Type listOfPrievanEventDTOs = new TypeToken<ArrayList<PrievanEventDTO>>() {
        }.getType();
        Gson gson = new Gson();

        return gson.fromJson(jsonString, listOfPrievanEventDTOs);
    }
}
