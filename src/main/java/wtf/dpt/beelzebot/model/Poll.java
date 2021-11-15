package wtf.dpt.beelzebot.model;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class Poll {

    private String question;
    private List<PollOption> answers;
    private String castBy;
    private String endedBy;
    private List<String> usersThatVoted;
    private Map<String, List<Integer>> votes = new HashMap<>();

    public PollResult getResults() {

        this.answers = answers.stream()
                .sorted(Comparator.comparing(PollOption::getVoteCount).reversed())
                .collect(Collectors.toList());

        List<String> answerStrings = new ArrayList<>();
        for (int i = 0; i < this.answers.size(); i++) {
            PollOption vote = this.answers.get(i);
            answerStrings.add(String.format("#%s %s - %s votes",
                    i + 1,
                    vote.getVoteString(),
                    vote.getVoteCount()
            ));
        }

        return new PollResult(
                this.question,
                calculateWinner(),
                answerStrings
        );
    }

    public String getResultText() {

        PollResult result = getResults();

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Vote '%s' is over - the winner is '%s'\n\n",
                result.getQuestion(),
                result.getWinner()
        ));
        for(String entry : result.getOrder()){
            sb.append(entry).append("\n");
        }

        return sb.toString();
    }

    private String calculateWinner(){

        List<PollOption> orderedPollOptions = this.answers;
        int highest = orderedPollOptions.stream()
                .mapToInt(PollOption::getVoteCount)
                .max()
                .orElse(-1);

        return orderedPollOptions.stream()
                .filter(o -> o.getVoteCount() == highest)
                .map(PollOption::getVoteString)
                .collect(Collectors.joining(", "));
    }

    public boolean alreadyVoted(String userID){
        return this.getVotes().containsKey(userID);
    }
}
