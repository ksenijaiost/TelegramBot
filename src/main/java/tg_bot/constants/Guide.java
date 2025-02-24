package tg_bot.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class Guide {
    private String name;
    private String description;
    private String link;
    private List<String> keywords;
}
