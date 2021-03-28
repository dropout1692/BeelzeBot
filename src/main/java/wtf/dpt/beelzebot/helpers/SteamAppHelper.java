package wtf.dpt.beelzebot.helpers;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import wtf.dpt.beelzebot.model.steam.SteamAppTuple;
import wtf.dpt.beelzebot.model.steam.SteamAppsDTO;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Scope("singleton")
public class SteamAppHelper {

    public static final String APPS_URL = "https://api.steampowered.com/ISteamApps/GetAppList/v2/";

    private final RestTemplate restTemplate;

    private List<SteamAppTuple> apps = new ArrayList<>();

    public SteamAppHelper(RestTemplateBuilder templateBuilder) {
        restTemplate = templateBuilder.build();
    }

    public List<SteamAppTuple> findAppID(String name) {

        initializeApps();

        SteamAppTuple exactMatch = null;
        for(SteamAppTuple app : apps){
            if(app.getName().equalsIgnoreCase(name)){
                exactMatch = app;
                break;
            }
        }

        Deque<SteamAppTuple> matches = apps.stream()
                .filter(t -> !t.getName().equalsIgnoreCase(name))
                .filter(t -> t.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toCollection(ArrayDeque::new));

        if(exactMatch != null){
            matches.addFirst(exactMatch);
        }

        return new ArrayList<>(matches);
    }

    private void initializeApps() {

        if (apps == null || apps.size() == 0) {
            SteamAppsDTO dto = getAppsFromAPI();
            this.apps = dto.getAppList().getApps().stream()
                    .sorted(Comparator.comparing(SteamAppTuple::getAppID))
                    .collect(Collectors.toList());
        }
    }

    private SteamAppsDTO getAppsFromAPI() {
        return restTemplate.getForObject(APPS_URL, SteamAppsDTO.class);
    }
}
