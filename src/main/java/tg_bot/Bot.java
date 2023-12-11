package tg_bot;

import tg_bot.rest.dao.ListOfChatId;
import tg_bot.rest.dao.ListOfSending;

import java.util.List;

public interface Bot {
    public List<String> sendMsg(List<ListOfSending> listOfSending);
    public List<String> oneMsg(List<ListOfChatId> chatIds, String text);
}
