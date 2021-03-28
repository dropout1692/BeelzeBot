package wtf.dpt.beelzebot.model.chuck;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChuckValueDTO {

    private int id;
    private String joke;
    private String[] categories;
}
