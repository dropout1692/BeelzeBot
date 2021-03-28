package wtf.dpt.beelzebot.helpers;

import wtf.dpt.beelzebot.model.prievan.PrievanEventDTO;
import wtf.dpt.beelzebot.service.PrievanService;

import java.time.format.DateTimeFormatter;
import java.util.List;

//todo: convert to a prototype bean
public class PrievanHelper {

    public String formMessage(List<PrievanEventDTO> events) {

        StringBuilder stringBuilder = new StringBuilder();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_TIME;

        stringBuilder.append(String.format("The last %s events were:\n\n", PrievanService.LIST_LIMIT));

        for (PrievanEventDTO event : events) {
            stringBuilder.append(String.format(" - %s %s %s at %s\n",
                    event.getUsername(),
                    event.getAction().getLabel(),
                    event.getChannel(),
                    dateFormatter.format(event.getTime())));
        }

        return stringBuilder.toString();
    }
}
