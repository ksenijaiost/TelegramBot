package tg_bot.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import tg_bot.constants.Guide;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class GuideManager {
    private List<Guide> guides;

    public GuideManager() {
        loadGuidesFromJson();
    }

    private void loadGuidesFromJson() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("guides.json")) {
            if (inputStream == null) {
                throw new RuntimeException("Файл guides.json не найден в resources!");
            }
            guides = mapper.readValue(inputStream, new TypeReference<>() {});
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке guides.json: " + e.getMessage(), e);
        }
    }

    public List<Guide> getAllGuides() {
        return guides;
    }

    public List<Guide> searchGuidesByKeyword(String keyword) {
        return guides.stream()
                .filter(guide -> guide.getKeywords().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}
