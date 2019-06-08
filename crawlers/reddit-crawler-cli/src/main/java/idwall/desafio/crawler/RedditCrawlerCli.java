package idwall.desafio.crawler;

import idwall.desafio.crawler.reddit.JSoupSubredditThreadIteratorFactory;
import idwall.desafio.crawler.reddit.RedditCrawlerImpl;
import idwall.desafio.crawler.reddit.RedditCrawlerProtos.RedditThreadInfo;

import java.util.List;
import java.util.stream.Collectors;

public class RedditCrawlerCli {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Este programa aceita como argumento uma lista de nomes de " +
                "subreddits separados por ; (ponto-e-vírgula). Ex: askreddit;worldnews;cats");
            System.exit(1);
        }

        String subredditList = args[0];

        RedditCrawlerImpl rs = new RedditCrawlerImpl(new JSoupSubredditThreadIteratorFactory());

        List<RedditThreadInfo> results = rs.getSubredditListTopThreads(subredditList, 5000)
                .collect(Collectors.toList());

        for(RedditThreadInfo rti: results) {
            System.out.println(rti.getTitle());
            System.out.println("Pontuação: " + rti.getScore());
            System.out.println("Subreddit: " + rti.getSubreddit());
            System.out.println("Link: " + rti.getLink());
            System.out.println(rti.getCommentsCount() + " comentário(s): " + rti.getCommentsLink());
            System.out.println();
        }
    }
}
