package wtf.dpt.beelzebot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SteamConfig {

    @Value("${steam.api.key}")
    private String token;
}
