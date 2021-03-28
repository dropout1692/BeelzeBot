package wtf.dpt.beelzebot.listeners;

import discord4j.core.event.domain.VoiceStateUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import wtf.dpt.beelzebot.model.prievan.PrievanEventDTO;
import wtf.dpt.beelzebot.service.PrievanService;

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

        if (eventDTO != null) {
            prievanService.addEvent(eventDTO);
        }

        return Mono.empty();
    }
}
