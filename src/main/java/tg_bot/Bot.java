package tg_bot;

import tg_bot.rest.dao.ListOfChatId;
import tg_bot.rest.dao.ListOfSending;

import java.util.List;

public interface Bot {
    List<String> sendMsg(List<ListOfSending> listOfSending);

    List<String> oneMsg(List<ListOfChatId> chatIds, String text);
}