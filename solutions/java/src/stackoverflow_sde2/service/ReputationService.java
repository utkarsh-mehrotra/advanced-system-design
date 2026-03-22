package stackoverflow_sde2.service;

import stackoverflow_sde2.event.VoteEvent;

/**
 * Acts as the Observer for any user engagement events across the system to calculate rep.
 * Perfectly decouples Reputation Math from the User POJO.
 */
public class ReputationService {

    // Hardcoded Rules externalized to the service, easily swappable
    private static final int QUESTION_UPVOTE = 5;
    private static final int QUESTION_DOWNVOTE = -5;
    private static final int ANSWER_UPVOTE = 10;
    private static final int ANSWER_DOWNVOTE = -10;
    private static final int ANSWER_ACCEPTED = 15;

    public void onVoteReceived(VoteEvent event) {
        int delta = 0;
        switch (event.getType()) {
            case QUESTION:
                delta = event.getVoteValue() > 0 ? QUESTION_UPVOTE : QUESTION_DOWNVOTE;
                break;
            case ANSWER:
                delta = event.getVoteValue() > 0 ? ANSWER_UPVOTE : ANSWER_DOWNVOTE;
                break;
            case COMMENT:
                // Comments rarely give rep in modern StackOverflow, but could be added easily here
                break;
        }

        event.getAuthorOfPost().updateReputation(delta);
    }
    
    public void onAnswerAccepted(stackoverflow_sde2.User author) {
        author.updateReputation(ANSWER_ACCEPTED);
    }
}
