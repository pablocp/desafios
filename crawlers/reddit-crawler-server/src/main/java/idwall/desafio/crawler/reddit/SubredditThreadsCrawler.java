package idwall.desafio.crawler.reddit;

import idwall.desafio.crawler.reddit.RedditCrawlerProtos.RedditThreadInfo;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SubredditThreadsCrawler implements Iterator<RedditThreadInfo> {

    public SubredditThreadsCrawler(String subredditUrl) {
        this.nextUrl = "https://old.reddit.com/r/" + subredditUrl + "/top";
        fetchedThreadInfos = new LinkedList<>();
    }

    @Override
    public boolean hasNext() {
        return !fetchedThreadInfos.isEmpty() || nextUrl != null;
    }

    @Override
    public RedditThreadInfo next() {
        if (fetchedThreadInfos.isEmpty()) {
            if (nextUrl == null) throw new NoSuchElementException();

            this.fetchNextThreadsPage();
        }
        return fetchedThreadInfos.poll();
    }

    private void fetchNextThreadsPage() {
        try {
            Document doc = Jsoup.connect(nextUrl).get();
            Elements threadsDivs = doc.getElementsByClass("thing");

            ThreadDivToRedditThreadInfoConverter converter =
                new ThreadDivToRedditThreadInfoConverter();

            for(Element threadDiv: threadsDivs){
                if (!threadDiv.hasClass("promoted"))
                    fetchedThreadInfos.add(converter.conver(threadDiv));
            }

            Element nextButton = doc.selectFirst("a[rel*=next]");
            if(nextButton == null) this.nextUrl = null;
            else this.nextUrl = nextButton.attributes().get("href");

        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    private String nextUrl;
    private LinkedList<RedditThreadInfo> fetchedThreadInfos;
}
