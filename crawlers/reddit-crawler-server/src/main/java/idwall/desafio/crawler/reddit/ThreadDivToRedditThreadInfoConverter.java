package idwall.desafio.crawler.reddit;

import idwall.desafio.crawler.reddit.RedditCrawlerProtos.RedditThreadInfo;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

public class ThreadDivToRedditThreadInfoConverter {
    public RedditThreadInfo conver(Element threadDiv) {
        RedditThreadInfo.Builder builder = RedditThreadInfo.newBuilder();

        Attributes attributes = threadDiv.attributes();

        trySetAuthor(builder, attributes.get("data-author"));
        trySetScore(builder, attributes.get("data-score"));
        trySetCommentsCount(builder, attributes.get("data-comments-count"));
        trySetSubreddit(builder, attributes.get("data-subreddit"));

        Element threadLink =
            threadDiv.getElementsByAttributeValue("data-event-action", "title").first();

        if (threadLink != null) {
            trySetLink(builder, threadLink.attributes().get("href"));
            trySetTitle(builder, threadLink.text());
        } else {
//            logger.warn("Missing thread link element");
        }

        Element commentsLink = threadDiv.getElementsByAttributeValue("data-event-action", "comments").first();
        if (commentsLink != null) {
            trySetCommentsLink(builder, commentsLink.attributes().get("href"));
        } else {
//            logger.warn("Missing comments link element");
        }

        return builder.build();
    }

    private void trySetCommentsLink(RedditThreadInfo.Builder builder, String link) {
        if (link == null) {
            link = "";
//            logger.warn("Null comments link");
        }
        builder.setCommentsLink(link);
    }

    private void trySetTitle(RedditThreadInfo.Builder builder, String title) {
        if (title == null) {
            title = "";
//            logger.warn("Null title");
        }
        builder.setTitle(title);
    }

    private void trySetLink(RedditThreadInfo.Builder builder, String link) {
        if (link == null) {
            link = "";
//            logger.warn("Null link");
        }
        builder.setLink(link);
    }

    private void trySetSubreddit(RedditThreadInfo.Builder builder, String subreddit) {
        if (subreddit == null) {
            subreddit = "";
//            logger.warn("Null subreddit");
        }
        builder.setSubreddit(subreddit);
    }

    private void trySetAuthor(RedditThreadInfo.Builder builder, String author) {
        if (author == null) {
            author = "Autor desconhecido";
//            logger.warn("Null author");
        }

        builder.setAuthor(author);
    }

    private void trySetScore(RedditThreadInfo.Builder builder, String scoreString) {
        try {
            builder.setScore(Integer.parseInt(scoreString));
        } catch (Exception ex) {
//            logger.warn("Invalid score: " + scoreString);
        }
    }

    private void trySetCommentsCount(RedditThreadInfo.Builder builder, String commentsCountString) {
        try {
            builder.setCommentsCount(Integer.parseInt(commentsCountString));
        } catch (Exception ex) {
//            logger.warn("Invalid comments count: " + commentsCountString);
        }
    }

//  private static final Logger logger =
//      LogManager.getLogger(ThreadDivToRedditThreadInfoConverter.class);
}
