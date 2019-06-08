package idwall.desafio.crawler.reddit;

import idwall.desafio.crawler.reddit.RedditCrawlerProtos.RedditThreadInfo;
import java.util.Iterator;

public class JSoupSubredditThreadIteratorFactory implements ISubredditThreadIteratorFactory {
    @Override
    public Iterator<RedditThreadInfo> create(String subreddit) {
        return new SubredditThreadsCrawler(subreddit);
    }
}
