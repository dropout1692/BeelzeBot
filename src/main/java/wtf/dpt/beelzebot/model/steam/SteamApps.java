package wtf.dpt.beelzebot.model.steam;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SteamApps {

    private List<SteamAppTuple> apps;
}
