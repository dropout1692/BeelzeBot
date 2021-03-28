package wtf.dpt.beelzebot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import wtf.dpt.beelzebot.config.SteamConfig;
import wtf.dpt.beelzebot.helpers.SteamAppHelper;
import wtf.dpt.beelzebot.model.steam.SteamAppTuple;

import java.util.List;

@Service
public class SteamService {

    private static final String BASE_URL = "http://api.steampowered.com/";

    @Autowired
    SteamConfig conf;

    @Autowired
    SteamAppHelper steamAppHelper;

    public String findAppID(String name) {

        List<SteamAppTuple> apps = steamAppHelper.findAppID(name);
        int others = 0;

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < apps.size(); i++) {
            if (i < 5) {
                SteamAppTuple curTuple = apps.get(i);
                stringBuilder.append(String.format("%s - %s\n",
                        curTuple.getAppID(),
                        curTuple.getName()));
            } else {
                others++;
            }
        }

        if (others > 0) {
            stringBuilder.append(String.format("..and %s other results.", others));
        }

        return stringBuilder.toString();
    }

    private String formURL(String urlString, MultiValueMap<String, String> params) {

        if (params == null) {
            params = new LinkedMultiValueMap<>();
        }

        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(BASE_URL)
                .path(urlString)
                .queryParam("key", conf.getToken())
                .queryParams(params)
                .build()
                .toString();
    }
}
