package wtf.dpt.beelzebot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PollResult {

    private String question;
    private String winner;
    private List<String> order;
}
