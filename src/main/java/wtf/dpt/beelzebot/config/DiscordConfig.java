package wtf.dpt.beelzebot.config;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wtf.dpt.beelzebot.listeners.EventListener;

import java.util.List;

@Configuration
public class DiscordConfig {

    @Value("${discord.api.key}")
    private String token;

    @Bean
    public <T extends Event> GatewayDiscordClient gatewayDiscordClient(List<EventListener<T>> eventListeners) {

        GatewayDiscordClient client = spawnClient();

        for (EventListener<T> listener : eventListeners) {
            if (client != null) {
                client.on(listener.getEventType())
                        .flatMap(listener::execute)
                        .onErrorResume(listener::handleError)
                        .retry()
                        .subscribe();
            } else {
                client = spawnClient();
            }
        }

        return client;
    }

    private GatewayDiscordClient spawnClient() {
        return DiscordClientBuilder.create(token)
                .build()
                .login()
                .block();
    }
}
