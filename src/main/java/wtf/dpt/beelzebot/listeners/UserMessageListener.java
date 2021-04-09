package wtf.dpt.beelzebot.listeners;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import wtf.dpt.beelzebot.helpers.HelpHelper;
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

    @Autowired
    NineGagService nineGagService;

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
        } else if (message.getContent().startsWith("!help")) {
            return executeHelp(message);
        } else if (message.getContent().startsWith("!9gag")) {
            return execute9Gag(message);
        }else if((message.getContent().startsWith("!report")){
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

    private Mono<Void> executeAbout(Message message) {

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
        for (String entry : helpHelper.getCommands()) {
            stringBuilder.append(String.format("\t%s\n", entry));
        }

        return Mono.just(message)
                .filter(msg -> msg.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(msg -> msg.getContent().equalsIgnoreCase("!help"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(stringBuilder.toString()))
                .then();
    }

    private Mono<Void> execute9Gag(Message message) {

        String url = message.getContent().replace("!9gag", "").strip();

        if (url.length() > 0) {
            return Mono.just(message)
                    .filter(msg -> msg.getAuthor().isPresent())
                    .filter(msg -> msg.getAuthor().map(user -> !user.isBot()).orElse(false))
                    .flatMap(Message::getChannel)
                    .flatMap(channel -> channel.createMessage(nineGagService.correct9GagLink(url, message)))
                    .then(Mono.just(message))
                    .flatMap(Message::delete)
                    .then();
        } else {
            return Mono.empty();
        }
    }

    @Override
    public Mono<Void> handleError(Throwable error) {
        log.error("Unable to handle MessageCreateEvent! Attempting to continue..", error);
        return Mono.empty();
    }
}
