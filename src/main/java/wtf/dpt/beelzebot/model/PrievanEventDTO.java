package wtf.dpt.beelzebot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import wtf.dpt.beelzebot.enums.PrievanAction;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PrievanEventDTO {

    private String username;
    private String channel;
    private LocalDateTime time;
    private PrievanAction action;
}
