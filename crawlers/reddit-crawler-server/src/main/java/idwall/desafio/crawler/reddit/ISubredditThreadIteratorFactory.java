package idwall.desafio.crawler.reddit;

import idwall.desafio.crawler.reddit.RedditCrawlerProtos.RedditThreadInfo;
import java.util.Iterator;

@FunctionalInterface
public interface ISubredditThreadIteratorFactory {
    Iterator<RedditThreadInfo> create(String subreddit);
}
