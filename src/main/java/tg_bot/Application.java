package tg_bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import tg_bot.bot.AcChatBot;

@SpringBootApplication
@EnableFeignClients
@Slf4j
public class Application {
    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new AcChatBot());
            System.out.println("Бот запущен!");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
