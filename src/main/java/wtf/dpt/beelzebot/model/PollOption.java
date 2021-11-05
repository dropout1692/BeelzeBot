package wtf.dpt.beelzebot.model;

import lombok.Getter;

@Getter
public class PollOption {

    int voteCount;
    String voteString;

    public PollOption(String voteString){
        this.voteCount = 0;
        this.voteString = voteString;
    }

    public void addVote(){
        voteCount++;
    }
}
