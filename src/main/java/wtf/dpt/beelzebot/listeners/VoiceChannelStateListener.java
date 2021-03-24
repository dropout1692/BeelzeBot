package wtf.dpt.beelzebot.listeners;

import discord4j.core.event.domain.VoiceStateUpdateEvent;
import discord4j.core.object.VoiceState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import wtf.dpt.beelzebot.enums.PrievanAction;
import wtf.dpt.beelzebot.model.PrievanEventDTO;
import wtf.dpt.beelzebot.service.PrievanService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class VoiceChannelStateListener implements EventListener<VoiceStateUpdateEvent> {

    @Autowired
    private PrievanService prievanService;

    @Override
    public Class<VoiceStateUpdateEvent> getEventType() {
        return VoiceStateUpdateEvent.class;
    }

    @Override
    public Mono<Void> execute(VoiceStateUpdateEvent event) {

        PrievanEventDTO eventDTO = prievanService.determineEvent(event);
        prievanService.addEvent(eventDTO);

        return Mono.empty();
    }
}
