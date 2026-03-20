package stackoverflow_upgraded.event;

import stackoverflow_upgraded.User;

public class VoteEvent {
    public enum TargetType { QUESTION, ANSWER, COMMENT }

    private final User authorOfPost;
    private final int voteValue; // +1 or -1
    private final TargetType type;

    public VoteEvent(User authorOfPost, int voteValue, TargetType type) {
        this.authorOfPost = authorOfPost;
        this.voteValue = voteValue;
        this.type = type;
    }

    public User getAuthorOfPost() { return authorOfPost; }
    public int getVoteValue() { return voteValue; }
    public TargetType getType() { return type; }
}
