package wtf.dpt.beelzebot.listeners;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import wtf.dpt.beelzebot.helpers.BotHelper;
import wtf.dpt.beelzebot.helpers.PrievanHelper;
import wtf.dpt.beelzebot.model.ChuckJokeDTO;
import wtf.dpt.beelzebot.model.PrievanEventDTO;
import wtf.dpt.beelzebot.service.ChuckService;
import wtf.dpt.beelzebot.service.PrievanService;

import java.util.List;

@Service
public class UserMessageListener implements EventListener<MessageCreateEvent> {

    final static Logger log = LoggerFactory.getLogger(UserMessageListener.class);

    @Autowired
    ChuckService chuckService;

    @Autowired
    PrievanService prievanService;

    @Autowired
    BotHelper botHelper;

    @Override
    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {

        final Message message = event.getMessage();

        switch (message.getContent()) {
            case "!chuck":
                return executeChuck(message);
            case "!prievan":
                return executePrievanList(message);
            case "!about":
                return executeLinkAbout(message);
            case "!help":
                return executeHelp(message);
            case "!report":
                return executeLinkIssues(message);
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

    private Mono<Void> executeLinkAbout(Message message) {

        return Mono.just(message)
                .filter(msg -> msg.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(msg -> msg.getContent().equalsIgnoreCase("!about"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(BotHelper.LINK_ABOUT))
                .then();
    }

    private Mono<Void> executeLinkIssues(Message message) {

        String issuesMessage = String.format(
                "If you wish to report an issue or to suggest an improvement please refer to:\n%s",
                    BotHelper.LINK_ISSUES);

        return Mono.just(message)
                .filter(msg -> msg.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(msg -> msg.getContent().equalsIgnoreCase("!report"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(issuesMessage))
                .then();
    }

    private Mono<Void> executeHelp(Message message) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Currently available commands:\n");
        for (String entry : botHelper.getCommands()) {
            stringBuilder.append(String.format("\t%s\n", entry));
        }

        return Mono.just(message)
                .filter(msg -> msg.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(msg -> msg.getContent().equalsIgnoreCase("!help"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(stringBuilder.toString()))
                .then();
    }

    @Override
    public Mono<Void> handleError(Throwable error) {
        log.error("Unable to handle MessageCreateEvent! Attempting to continue..", error);
        return Mono.empty();
    }
}
