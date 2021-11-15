package wtf.dpt.beelzebot.helpers;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import wtf.dpt.beelzebot.service.PrievanService;

import java.util.List;

@Getter
@Service
public class BotHelper {

    @Value("${bot.version}")
    public static String BOT_VERSION;

    public static final String LINK_ABOUT = "https://github.com/dropout1692/BeelzeBot/tree/master";
    public static final String LINK_ISSUES = "https://github.com/dropout1692/BeelzeBot/issues";

    public static String getAbout() {
        return String.format("%s\n%s",
                BOT_VERSION,
                LINK_ABOUT
        );
    }

    private final List<String> commands = List.of(
            "!help              - shows currently available commands",
            "!report            - links to the issue reporting page",
            "!about             - links to GitHub/master",
            "!prievan           - shows last " + PrievanService.LIST_LIMIT + " voice channel events",
            "!chuck             - posts a savage Chuck Norris joke",
            "!9gag <link>       - extracts a meme from a 9gag link; if it's a video it gets embedded",
            "!vote <number>     - vote on the currently active poll"
    );
}
