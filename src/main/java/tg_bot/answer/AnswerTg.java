package tg_bot.answer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnswerTg {
    /*
    {
    "ok": true,
    "result": {
        "message_id": 2248,
        "from": {
            "id": 6745573437,
            "is_bot": true,
            "first_name": "Бот для Тайного Санты среди брейдеров",
            "username": "dawn_snowflake_bot"
        },
        "chat": {
            "id": 882440120,
            "first_name": "Ta",
            "last_name": "MaRa",
            "username": "TamaraKaza",
            "type": "private"
        },
        "date": 1699802751,
        "text": "Это особенности гугл таблиц, ничего страшного, Санта скопирует и откроет)"
    }
}
     */
    @JsonProperty("ok")
    private String ok;

    @JsonProperty("result")
    private AnswerResult result;
}
