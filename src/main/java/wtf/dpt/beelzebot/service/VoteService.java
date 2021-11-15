package wtf.dpt.beelzebot.service;

import discord4j.core.object.entity.Message;
import lombok.Getter;
import org.springframework.stereotype.Service;
import wtf.dpt.beelzebot.model.Poll;
import wtf.dpt.beelzebot.model.PollOption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoteService {

    //todo: extract strings
    //todo: vote valid options set
    //todo: enable/handle multi-polling && repeated votes

    @Getter
    private Poll currentPoll = null;
    private Poll previousPoll = null;

    public String castPoll(String userName, Message message) {
        return castPoll(parsePoll(userName, message));
    }

    public String castPoll(Poll poll) {

        if (poll.getQuestion().length() != 0) {

            if (this.currentPoll != null) {
                return String.format("There is an active poll already.\n%s",
                        getPollString()
                );
            }

            if (poll.getAnswers().size() < 2) {
                return "Not enough options to vote on!";
            }

            this.currentPoll = poll;

        } else {
            if (this.currentPoll == null) {
                return "There is no active poll.";
            }
        }

        return getPollString();
    }

    public String castVote(Message message) {

        if (this.currentPoll == null) {
            return "There is no active poll.";
        }

        String username = message.getUserData().username();
        String userID = message.getUserData().id().asString();

        String voteString = message.getContent().replace("!vote", "").strip();
        if (!voteString.matches("(\\d+\\s)+")) {
            return getVotingHelp();
        }

        List<Integer> newVotes = Arrays.stream(voteString.split("\\s"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        if (this.currentPoll.alreadyVoted(userID)) {

            List<Integer> oldVotes = this.currentPoll.getVotes().get(userID);
            for (int vote : oldVotes) {
                this.currentPoll.getAnswers().get(vote - 1).removeVote();
            }
        }

        for (int vote : newVotes) {

            if (vote - 1 > this.currentPoll.getAnswers().size() || vote < 1 || vote > this.currentPoll.getAnswers().size()) {
                return getVotingHelp();
            }

            this.currentPoll.getAnswers().get(vote - 1).addVote();
        }

        return String.format("Vote accepted from %s! Use !poll to display the current poll.",
                username
        );
    }

    public String endPoll(String userName) {

        Poll currentPoll = this.currentPoll;

        if (currentPoll == null) {
            return "There is no active poll.";
        }

        currentPoll.setEndedBy(userName);
        String resultText = currentPoll.getResultText();
        String pollResults = String.format("Poll ended! The results are:\n\n%s", resultText);

        this.previousPoll = this.currentPoll;
        this.currentPoll = null;

        return pollResults;
    }

    public String printPreviousPoll() {
        if (this.previousPoll != null) {
            return "The previous poll was:\n\n" + getPollString(true);
        } else {
            return "There was no poll yet.";
        }
    }

    private String getPollString() {
        return getPollString(false);
    }

    private String getPollString(boolean previous) {

        StringBuilder response = new StringBuilder();
        Poll poll;
        if (previous) {
            response.append("The previous poll was:\n\n");
            poll = this.previousPoll;
        } else {
            poll = this.currentPoll;
        }

        response.append(String.format("%s cast by %s\n\n",
                poll.getQuestion(),
                poll.getCastBy()
        ));

        List<PollOption> pollOptions = poll.getAnswers();
        for (PollOption option : pollOptions) {
            String voteNumber = (option.getVoteCount() == 1) ? "1 vote" : option.getVoteCount() + " votes";
            response.append(String.format("%s - %s\n",
                    option.getVoteString(),
                    voteNumber
            ));
        }

        return response.toString();
    }

    private Poll parsePoll(String userName, Message message) {

        String voteString = message.getContent().replace("!poll", "").strip();
        List<String> lines = Arrays.asList(voteString.split("\n"));

        Poll vote = new Poll();
        vote.setCastBy(userName);
        vote.setQuestion(lines.get(0));
        List<PollOption> voteOptions = new ArrayList<>();
        if (lines.size() > 1) {
            for (int i = 1; i < lines.size(); i++) {
                voteOptions.add(new PollOption(lines.get(i)));
            }
        }
        vote.setAnswers(voteOptions);

        return vote;
    }

    private String getVotingHelp() {

        return "Illegal vote! To vote use the !vote command followed by the number of your choice.\n" +
                "For example \"!vote 1\" for the first option.";
    }
}
