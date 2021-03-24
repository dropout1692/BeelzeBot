package wtf.dpt.beelzebot.service;

import org.springframework.stereotype.Service;
import wtf.dpt.beelzebot.model.PrievanEventDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrievanService {

    public static final int LIST_LIMIT = 10;

    List<PrievanEventDTO> prievanList = new ArrayList<>();

    public void addEvent(PrievanEventDTO dto) {

        prievanList.add(dto);

        //keep only {LIST_LIMIT} items
        if (prievanList.size() > LIST_LIMIT) {
            prievanList = prievanList.subList(prievanList.size() - LIST_LIMIT, prievanList.size());
        }
    }

    public List<PrievanEventDTO> getEvents() {
        return prievanList;
    }
}
