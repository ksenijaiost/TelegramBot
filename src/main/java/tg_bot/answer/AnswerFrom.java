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
    @JsonProperty("id")
    private String id;

    @JsonProperty("is_bot")
    private String is_bot;

    @JsonProperty("first_name")
    private String first_name;

    @JsonProperty("username")
    private String username;
}
