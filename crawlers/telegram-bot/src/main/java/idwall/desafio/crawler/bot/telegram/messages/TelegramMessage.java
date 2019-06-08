package idwall.desafio.crawler.bot.telegram.messages;

public class TelegramMessage {

    public TelegramChat getChat() {
        return this.chat;
    }

    public void setChat(TelegramChat chat) {
        this.chat = chat;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private TelegramChat chat;
    private String text;

}
