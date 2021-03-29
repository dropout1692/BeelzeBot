package wtf.dpt.beelzebot.service;

import discord4j.core.event.domain.VoiceStateUpdateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.VoiceChannel;
import org.springframework.stereotype.Service;
import wtf.dpt.beelzebot.enums.PrievanAction;
import wtf.dpt.beelzebot.model.PrievanEventDTO;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PrievanService {

    public static final int LIST_LIMIT = 10;

    List<PrievanEventDTO> prievanList = new ArrayList<>();

    public void addEvent(PrievanEventDTO dto) {

        prievanList.add(dto);

        //keep only {LIST_LIMIT} items
        if (prievanList.size() > LIST_LIMIT) {
            prievanList = prievanList.subList(prievanList.size() - LIST_LIMIT, prievanList.size());
        }
    }

    public List<PrievanEventDTO> getEvents() {
        return prievanList;
    }

    public PrievanEventDTO determineEvent(VoiceStateUpdateEvent event) {

        PrievanEventDTO dto = new PrievanEventDTO();

        VoiceState currentState = event.getCurrent();
        Optional<VoiceState> oldState = event.getOld();

        dto.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        //todo: resolve thread blocking
        User user = currentState.getUser().block();
        if (user != null) {
            dto.setUsername(user.getUsername());
        }

        PrievanAction action = determineAction(currentState, oldState);
        if (action == null) { //switch bug fix
            return null;
        }
        dto.setAction(action);

        VoiceChannel channel = null;
        switch (action) {
            case JOIN:
            case SWITCH:
                channel = currentState.getChannel().block();
                break;
            case LEFT:
                assert oldState.isPresent();
                channel = oldState.get().getChannel().block();
                break;
        }

        if (channel != null) {
            dto.setChannel(channel.getName());
        }

        return dto;
    }

    private PrievanAction determineAction(VoiceState currentState, Optional<VoiceState> oldState) {

        if (oldState.isEmpty()) {
            return PrievanAction.JOIN;
        } else if (currentState.getChannelId().isEmpty()) {
            return PrievanAction.LEFT;
        } else {
            if (currentState.getChannelId().equals(oldState.get().getChannelId())) { //not an actual switch
                return null;
            } else {
                return PrievanAction.SWITCH;
            }
        }
    }
}
