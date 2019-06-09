package idwall.desafio.crawler.bot.telegram;

import com.fasterxml.jackson.databind.ObjectMapper;
import idwall.desafio.crawler.bot.telegram.messages.TelegramBotMessage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.springframework.stereotype.Component;

@Component
public class TelegramBotApiClient {
    public TelegramBotApiClient() {
        String botToken = System.getenv(BOT_TOKEN_ENV_VAR);
        this.botChatUrl = String.format(BOT_CHAT_URL_TEMPLATE, botToken);
    }

    public boolean sendMessage(String message, int chatId) {
        TelegramBotMessage botMessage = new TelegramBotMessage();
        botMessage.setChatId(chatId);
        botMessage.setText(message);
        botMessage.setMethod("sendMessage");
        botMessage.setParseMode("Markdown");
        try {
            return sendBotRequest(botMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean sendBotRequest(Object payload) throws Exception {
        URL obj = new URL(this.botChatUrl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// For POST only - START
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        ObjectMapper objectMapper = new ObjectMapper();
		OutputStream os = con.getOutputStream();
		os.write(objectMapper.writeValueAsBytes(payload));
		os.flush();
		os.close();
		// For POST only - END

		int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            return true;
        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            //System.out.println(br.lines().collect(Collectors.joining()));
            return false;
        }
    }

    private String botChatUrl;
    private static final String BOT_TOKEN_ENV_VAR = "TELEGRAM_BOT_TOKEN";
    private static final String BOT_CHAT_URL_TEMPLATE =
        "https://api.telegram.org/bot%s/sendMessage";
}
