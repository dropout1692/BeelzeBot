package wtf.dpt.beelzebot.service;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class NineGagService {

    public String correct9GagLink(String url, Message message){

        boolean isVideo = isVideoLink(url);

        Optional<User> user = message.getAuthor();
        assert user.isPresent();
        String userMention = user.get().getMention();

        //todo: permissions need to be resolved
        message.delete();

        String fixedUrl = (isVideo ? fixVideoLink(url) : fixPageLink(url));

        return formResponse(userMention, fixedUrl);
    }

    private String formResponse(String username, String fixedUrl) {

        return String.format("%s posted the following meme:\n%s", username, fixedUrl);
    }

    //todo:impl
    private String fixVideoLink(String url){
        return url.replaceAll("\\.mp4", ".webm");
    }

    //todo:impl
    private String fixPageLink(String url){
        return "<funny link here soon>";
    }

    private boolean isVideoLink(String url){
        return Pattern.matches("^.*(\\.mp4)$", url);
    }
}
