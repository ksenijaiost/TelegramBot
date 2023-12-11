package tg_bot.rest.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListOfSending {
    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("text")
    private String text;
}
