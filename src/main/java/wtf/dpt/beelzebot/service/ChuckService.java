package wtf.dpt.beelzebot.service;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wtf.dpt.beelzebot.model.ChuckJokeDTO;

@Service
public class ChuckService {

    private static final String CHUCK_RANDOM_URL = "http://api.icndb.com/jokes/random";

    private final RestTemplate restTemplate;

    public ChuckService(RestTemplateBuilder templateBuilder) {
        this.restTemplate = templateBuilder.build();
    }

    public ChuckJokeDTO getChuckJoke() {

        ChuckJokeDTO dto = restTemplate.getForObject(CHUCK_RANDOM_URL, ChuckJokeDTO.class);

        //unescape HTML entities like &quot;
        if(dto != null && dto.getValue() != null){
            dto.getValue().setJoke(StringEscapeUtils.unescapeHtml4(dto.getValue().getJoke()));
        }

        return dto;
    }
}
