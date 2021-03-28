package wtf.dpt.beelzebot.model.steam;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SteamAppsDTO {

    @JsonProperty("applist")
    private SteamApps appList;
}
