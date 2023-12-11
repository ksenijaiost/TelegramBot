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
public class AnswerFrom {
    /*
    "from": {
            "id": 6745573437,
            "is_bot": true,
            "first_name": "Бот для Тайного Санты среди брейдеров",
            "username": "dawn_snowflake_bot"
        },
     */
    @JsonProperty("id")
    private String id;

    @JsonProperty("is_bot")
    private String is_bot;

    @JsonProperty("first_name")
    private String first_name;

    @JsonProperty("username")
    private String username;
}
