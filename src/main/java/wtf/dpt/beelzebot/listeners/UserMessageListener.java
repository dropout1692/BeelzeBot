package wtf.dpt.beelzebot.listeners;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import wtf.dpt.beelzebot.helpers.PrievanHelper;
import wtf.dpt.beelzebot.model.ChuckJokeDTO;
import wtf.dpt.beelzebot.model.PrievanEventDTO;
import wtf.dpt.beelzebot.service.ChuckService;
import wtf.dpt.beelzebot.service.PrievanService;

import java.util.List;

@Service
public class UserMessageListener implements EventListener<MessageCreateEvent> {

    @Autowired
    ChuckService chuckService;

    @Autowired
    PrievanService prievanService;

    @Override
    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {

        final Message message = event.getMessage();

        switch (message.getContent()){
            case "!chuck":
                return executeChuck(message);
            case "!prievan":
                return executePrievanList(message);
        }

        return Mono.empty();
    }

    private Mono<Void> executeChuck(Message message){

        ChuckJokeDTO jokeDTO = chuckService.getChuckJoke();

        return Mono.just(message)
                .filter(msg -> msg.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(msg -> msg.getContent().equalsIgnoreCase("!chuck"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(jokeDTO.getValue().getJoke()))
                .then();
    }

    private Mono<Void> executePrievanList(Message message){

        List<PrievanEventDTO> events = prievanService.getEvents();
        PrievanHelper helper = new PrievanHelper();

        return Mono.just(message)
                .filter(msg -> msg.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(msg -> msg.getContent().equalsIgnoreCase("!prievan"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(helper.formMessage(events)))
                .then();
    }
}
