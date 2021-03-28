package wtf.dpt.beelzebot.model.steam;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SteamAppTuple {

    @JsonProperty("appid")
    private String appID;

    private String name;
}
