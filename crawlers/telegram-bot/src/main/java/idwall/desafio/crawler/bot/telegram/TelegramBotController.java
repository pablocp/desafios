package idwall.desafio.crawler.bot.telegram;

import idwall.desafio.crawler.bot.telegram.messages.TelegramUpdate;
import idwall.desafio.crawler.reddit.RedditCrawlerProtos.RedditCrawlerService;
import idwall.desafio.crawler.reddit.RedditCrawlerProtos.GetTrendingRequest;
import idwall.desafio.crawler.reddit.RedditCrawlerProtos.GetTrendingResponse;
import idwall.desafio.crawler.reddit.RedditCrawlerProtos.RedditThreadInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/telegram")
public class TelegramBotController {

    @PostMapping("/")
    public void handleUpdate(@RequestBody TelegramUpdate update)
        throws Exception {

        String message = update.getMessage().getText();
        int chatId = update.getMessage().getChat().getId();

        if (message.equals("/Start")) {
            telegramBotApiClient.sendMessage(WELCOME_MESSAGE, chatId);
        } else if (message.startsWith("/NadaParaFazer")) {
            String commandParams = message.substring(14).trim();

            if (!commandParams.isEmpty()) {
                String[] subreddits = update.getMessage().getText().split(";");

                for (String subreddit: subreddits) {
                    sendTrendingThreads(subreddit, update.getMessage().getChat().getId());
                }

            } else {
                telegramBotApiClient.sendMessage(HELP_MESSAGE, chatId);
            }
        } else {
            telegramBotApiClient.sendMessage(HELP_MESSAGE, chatId);
        }
    }

    private void sendTrendingThreads(String subreddit, int chatId) {
        GetTrendingRequest request = GetTrendingRequest.newBuilder()
            .setSubreddit(subreddit)
            .setMinScore(5000)
            .build();

        redditCrawlerService.getTrending(null, request, response -> {
            telegramBotApiClient.sendMessage(createBotMessageFromResponse(response), chatId);
        });
    }

    private String createBotMessageFromResponse(GetTrendingResponse response) {
        StringBuilder sb = new StringBuilder();

        for (RedditThreadInfo rti : response.getRedditThreadInfoListList()) {
            sb.append("\n[");
            sb.append(rti.getTitle());
            sb.append("](");
            sb.append(rti.getLink());
            sb.append(")\n");
            sb.append(rti.getScore());
            sb.append(" pontos.\nAutor(a): ");
            sb.append(rti.getAuthor());
            sb.append("\n[");
            sb.append(rti.getCommentsCount());
            sb.append(" comentários](");
            sb.append(rti.getCommentsLink());
            sb.append(")\n");
        }

        return sb.toString();
    }

    @Autowired
    private RedditCrawlerService redditCrawlerService;

    @Autowired
    private TelegramBotApiClient telegramBotApiClient;

    private String WELCOME_MESSAGE = "Bem vinda(o)!";
    private String HELP_MESSAGE = "Digite o comando */NadaParaFazer* seguido de " +
        "uma lista de nomes de subreddits, separados por ponto-e-vírgula, " +
        "para ver as postagens mais comentadas daqueles temas!\nEx: " +
        "*/NadaParaFazer* cats;dogs;sports";
}
