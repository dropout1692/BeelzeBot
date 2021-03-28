package wtf.dpt.beelzebot.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wtf.dpt.beelzebot.model.chuck.ChuckJokeDTO;

@Service
public class ChuckService {

    private static final String CHUCK_RANDOM_URL = "http://api.icndb.com/jokes/random";

    private final RestTemplate restTemplate;

    public ChuckService(RestTemplateBuilder templateBuilder) {
        this.restTemplate = templateBuilder.build();
    }

    public ChuckJokeDTO getChuckJoke() {
        return restTemplate.getForObject(CHUCK_RANDOM_URL, ChuckJokeDTO.class);
    }
}
