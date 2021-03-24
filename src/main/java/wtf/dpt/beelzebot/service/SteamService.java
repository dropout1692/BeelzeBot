package wtf.dpt.beelzebot.service;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import wtf.dpt.beelzebot.config.SteamConfig;

@Service
public class SteamService {

    SteamConfig conf;

    private final String BASE_URL = "http://api.steampowered.com/";

    public SteamService(SteamConfig conf) {
        this.conf = conf;
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
