package tg_bot.answer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@FeignClient(value = "tgApi", url = "https://api.telegram.org/bot6745573437:AAEUEW3g-iXry0CFhZsTcrzTXSvUuag4vLk/")
public interface FeignTg {
    /**
     * Отправка рест запроса - отправка сообщения.
     *
     * @param chatId - id чата.
     * @param text   - строка, которую необходимот отправить в качестве сообщения.
     * @return - ответ на запрос.
     */
    @PostMapping(value = "/sendMessage", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    AnswerTg sendMsgTg(@PathVariable(value = "chat_id") String chatId, @PathVariable(value = "text") String text);
}