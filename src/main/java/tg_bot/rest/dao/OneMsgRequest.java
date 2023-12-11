package tg_bot.rest.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OneMsgRequest {
    @JsonProperty("list")
    private List<ListOfChatId> chatIds;

    @JsonProperty("text")
    private String text;
}
