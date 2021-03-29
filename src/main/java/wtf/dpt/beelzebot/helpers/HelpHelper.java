package wtf.dpt.beelzebot.helpers;

import lombok.Getter;
import org.springframework.stereotype.Service;
import wtf.dpt.beelzebot.service.PrievanService;

import java.util.List;

@Getter
@Service
public class HelpHelper {

    private final List<String> commands = List.of(
            "!help      - shows currently available commands",
            "!about     - links to GitHub/master",
            "!prievan   - shows last " + PrievanService.LIST_LIMIT + " voice channel events",
            "!chuck     - posts a savage Chuck Norris joke"
    );
}
