package wtf.dpt.beelzebot.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SteamConfig {

    @Value("${steam.api.key}")
    @Getter
    private String token;

    @Value("${steam.api.domain}")
    private String domain;
}
