package wtf.dpt.beelzebot.service;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class NineGagService {

    //todo: handle differently
    public static final String BROKEN_LINK = "<BROKEN LINK>";

    public static final String PHOTO_URL_BASE = "https://img-9gag-fun.9cache.com/photo/";

    public String correct9GagLink(String url, Message message) {

        boolean isVideo = isVideoLink(url);

        Optional<User> user = message.getAuthor();
        assert user.isPresent();
        String userMention = user.get().getMention();

        String fixedUrl = (isVideo ? fixVideoLink(url) : fixPageLink(url));

        return formResponse(userMention, fixedUrl);
    }

    private String formResponse(String username, String fixedUrl) {

        return String.format("%s posted the following meme:\n%s", username, fixedUrl);
    }

    private String fixVideoLink(String url) {
        return url.replaceAll("460svav1", "460svvp9");
    }

    private String fixPageLink(String url) {

        String predictedUrl = predictImageUrl(url);

        return (pingSource(predictedUrl) ? predictedUrl : BROKEN_LINK);
    }

    private boolean isVideoLink(String url) {
        return Pattern.matches("^.*(\\.webm|\\.mp4)$", url);
    }

    private String predictImageUrl(String url) {

        List<String> urlSplit = Arrays.asList(url.split("/"));
        return String.format("%s%s_700bwp.webp",
                PHOTO_URL_BASE,
                urlSplit.get(urlSplit.size() - 1));
    }

    private boolean pingSource(String urlString) {

        HttpURLConnection connection;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            return connection.getResponseCode() == 200;
        } catch (IOException ignored) {
            //todo: handle
        }

        return false;
    }
}
