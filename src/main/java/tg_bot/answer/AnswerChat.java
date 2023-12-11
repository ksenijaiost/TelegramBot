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
public class AnswerChat {
    @JsonProperty("id")
    private String id;

    @JsonProperty("first_name")
    private String first_name;

    @JsonProperty("last_name")
    private String last_name;

    @JsonProperty("username")
    private String username;

    @JsonProperty("type")
    private String type;

    public AnswerChat(String chatId) {
        this.id = chatId;
    }
}
