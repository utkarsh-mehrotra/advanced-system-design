package linkedin_sde3;

import java.util.List;
import java.util.Comparator;

public interface FeedStrategy {
    void sortFeed(List<FeedItem> feed);
}

class ChronologicalFeedStrategy implements FeedStrategy {
    @Override
    public void sortFeed(List<FeedItem> feed) {
        feed.sort(Comparator.comparingLong(FeedItem::getTimestamp).reversed());
    }
}
