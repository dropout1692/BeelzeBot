package wtf.dpt.beelzebot.listeners;

import discord4j.core.event.domain.VoiceStateUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import wtf.dpt.beelzebot.model.PrievanEventDTO;
import wtf.dpt.beelzebot.service.PrievanService;

@Service
public class VoiceChannelStateListener implements EventListener<VoiceStateUpdateEvent> {

    final static Logger log = LoggerFactory.getLogger(VoiceChannelStateListener.class);

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

    @Override
    public Mono<Void> handleError(Throwable error) {
        log.error("Unable to handle VoiceStateUpdateEvent! Attempting to continue..", error);
        return Mono.empty();
    }
}
