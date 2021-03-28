package wtf.dpt.beelzebot.listeners;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import wtf.dpt.beelzebot.helpers.PrievanHelper;
import wtf.dpt.beelzebot.model.chuck.ChuckJokeDTO;
import wtf.dpt.beelzebot.model.prievan.PrievanEventDTO;
import wtf.dpt.beelzebot.service.ChuckService;
import wtf.dpt.beelzebot.service.PrievanService;
import wtf.dpt.beelzebot.service.SteamService;

import java.util.List;

@Service
public class UserMessageListener implements EventListener<MessageCreateEvent> {

    @Autowired
    ChuckService chuckService;

    @Autowired
    PrievanService prievanService;

    @Autowired
    SteamService steamService;

    private final String ABOUT_LINK = "https://github.com/dropout1692/BeelzeBot/tree/master";

    @Override
    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {

        final Message message = event.getMessage();

        if (message.getContent().startsWith("!chuck")) {
            return executeChuck(message);
        } else if (message.getContent().startsWith("!prievan")) {
            return executePrievanList(message);
        } else if (message.getContent().startsWith("!about")) {
            return executeAbout(message);
        } else if (message.getContent().startsWith("!steamapp")) {
            return executeSteamApp(message);
        }

        return Mono.empty();
    }

    private Mono<Void> executeChuck(Message message) {

        ChuckJokeDTO jokeDTO = chuckService.getChuckJoke();

        return Mono.just(message)
                .filter(msg -> msg.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(msg -> msg.getContent().equalsIgnoreCase("!chuck"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(jokeDTO.getValue().getJoke()))
                .then();
    }

    private Mono<Void> executePrievanList(Message message) {

        List<PrievanEventDTO> events = prievanService.getEvents();
        PrievanHelper helper = new PrievanHelper();

        return Mono.just(message)
                .filter(msg -> msg.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(msg -> msg.getContent().equalsIgnoreCase("!prievan"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(helper.formMessage(events)))
                .then();
    }

    private Mono<Void> executeAbout(Message message) {

        return Mono.just(message)
                .filter(msg -> msg.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(msg -> msg.getContent().equalsIgnoreCase("!about"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(ABOUT_LINK))
                .then();
    }

    private Mono<Void> executeSteamApp(Message message) {

        String name = message.getContent().replace("!steamapp", "").strip();

        if (name.length() > 0) {
            return Mono.just(message)
                    .filter(msg -> msg.getAuthor().map(user -> !user.isBot()).orElse(false))
                    .flatMap(Message::getChannel)
                    .flatMap(channel -> channel.createMessage(steamService.findAppID(name)))
                    .then();
        } else {
            return Mono.empty();
        }
    }
}
