package idwall.desafio.crawler.bot.telegram.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TelegramBotMessage {

    public int getChatId() {
        return this.chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getParseMode() {
        return this.parseMode;
    }

    public void setParseMode(String parseMode) {
        this.parseMode = parseMode;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @JsonProperty("chat_id")
    private int chatId;

    @JsonProperty("parse_mode")
    private String parseMode;

    private String text;
    private String method;

}
