package idwall.desafio.crawler.reddit;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import idwall.desafio.crawler.reddit.RedditCrawlerProtos.GetTrendingRequest;
import idwall.desafio.crawler.reddit.RedditCrawlerProtos.GetTrendingResponse;
import idwall.desafio.crawler.reddit.RedditCrawlerProtos.RedditCrawlerService;
import idwall.desafio.crawler.reddit.RedditCrawlerProtos.RedditThreadInfo;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RedditCrawlerImpl implements RedditCrawlerService.Interface {

    public RedditCrawlerImpl(ISubredditThreadIteratorFactory subredditThreadIteratorFactory) {
        this.subredditThreadIteratorFactory = subredditThreadIteratorFactory;
    }

    public void getTrending(RpcController controller, GetTrendingRequest request,
        RpcCallback<GetTrendingResponse> done) {
        Stream<RedditThreadInfo> topThreads = this.getSubredditTopThreads(
            request.getSubreddit(), request.getMinScore());

        GetTrendingResponse response = GetTrendingResponse.newBuilder()
            .addAllRedditThreadInfoList(topThreads.collect(Collectors.toList()))
            .build();

        done.run(response);
     }

    public Stream<RedditThreadInfo> getSubredditListTopThreads(String subredditList, int minScore) {
        return Stream.of(subredditList.split(";"))
                .distinct()
                .flatMap(subreddit -> this.getSubredditTopThreads(subreddit, minScore));
    }

    private Stream<RedditThreadInfo> getSubredditTopThreads(String subreddit, int minScore) {
        Iterable<RedditThreadInfo> iterable =
                () -> this.subredditThreadIteratorFactory.create(subreddit);

        return StreamSupport.stream(iterable.spliterator(), false)
                .takeWhile(thread -> thread.getScore() >= minScore);
    }

    private final ISubredditThreadIteratorFactory subredditThreadIteratorFactory;

}
