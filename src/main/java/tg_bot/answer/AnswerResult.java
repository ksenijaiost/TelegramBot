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
public class AnswerResult {
    @JsonProperty("message_id")
    private String message_id;

    @JsonProperty("from")
    private AnswerFrom from;

    @JsonProperty("chat")
    private AnswerChat chat;

    @JsonProperty("date")
    private String date;

    @JsonProperty("text")
    private String text;

    public AnswerResult(AnswerChat answerChat) {
        this.chat = answerChat;
    }
}
