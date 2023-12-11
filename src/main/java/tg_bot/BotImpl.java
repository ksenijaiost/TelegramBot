package tg_bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tg_bot.answer.AnswerChat;
import tg_bot.answer.AnswerResult;
import tg_bot.answer.AnswerTg;
import tg_bot.answer.FeignTg;
import tg_bot.rest.dao.ListOfChatId;
import tg_bot.rest.dao.ListOfSending;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BotImpl implements Bot {
    private final FeignTg feignTg;

    @Override
    public List<String> sendMsg(List<ListOfSending> listOfSending) {
        List<String> answerTgList = new ArrayList<>();
        for (ListOfSending sending : listOfSending) {
            var answerTg = sendFeign(sending.getChatId(), sending.getText());
            answerTgList.add("id: " + answerTg.getResult().getChat().getId()
                    + ", username: " + answerTg.getResult().getChat().getUsername()
                    + ", text: " + answerTg.getResult().getText());
        }
        return answerTgList;
    }

    @Override
    public List<String> oneMsg(List<ListOfChatId> chatIds, String text) {
        List<String> answerTgList = new ArrayList<>();
        for (ListOfChatId chatId : chatIds) {
            var answerTg = sendFeign(chatId.getChatId(), text);
            answerTgList.add("id: " + answerTg.getResult().getChat().getId()
                    + ", username: " + answerTg.getResult().getChat().getUsername()
                    + ", text: " + answerTg.getResult().getText());
        }
        return answerTgList;
    }

    /**
     * Метод для формирования фейн-запроса в телеграм-апи + логирование.
     *
     * @param chatId - id чата телеграм.
     * @param text   - текст для отпраки.
     * @return - ответ от телеграм-апи.
     */
    private AnswerTg sendFeign(String chatId, String text) {
        log.info("Sending msg for {}, text - {}", chatId, text);
        var answer = Optional.ofNullable(feignTg.sendMsgTg(chatId, text))
                .orElse(new AnswerTg("not ok", new AnswerResult(new AnswerChat(chatId))));
        log.info("Answer: {}", answer);
        return answer;
    }
}