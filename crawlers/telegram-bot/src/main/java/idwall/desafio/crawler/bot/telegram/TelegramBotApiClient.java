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
        URL obj = new URL(BOT_CHAT_URL);
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

    private static final String BOT_TOKEN = "<INSERT_YOUR_BOT_TOKEN_HERE>";
    private static final String BOT_CHAT_URL =
        "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage";
}
