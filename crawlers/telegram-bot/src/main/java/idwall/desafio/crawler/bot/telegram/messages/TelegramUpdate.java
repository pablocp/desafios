package idwall.desafio.crawler.bot.telegram.messages;

public class TelegramUpdate {

    public TelegramMessage getMessage() {
        return this.message;
    }

    public void setTelegramMessage(TelegramMessage message) {
        this.message = message;
    }

    public int getUpdateId() {
        return this.updateId;
    }

    public void setUpdateId(int updateId) {
        this.updateId = updateId;
    }

    private int updateId;
    private TelegramMessage message;

}
