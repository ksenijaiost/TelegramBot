package tg_bot.functions;

import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg_bot.KeywordBot;

@AllArgsConstructor
public class AdminManager {
    private final KeywordBot bot;

    public void handleAdminCommand(long chatId, String text, Message message) {
        if (!BotUtils.isAdmin(chatId)) {
            BotUtils.sendMessage(chatId, "У вас нет прав для этой команды.", bot);
            return;
        }

        switch (text) {
            case "/reset":
                resetContest(chatId);
                break;
            case "/updateinfo":
                BotUtils.sendMessage(chatId, "Введите данные о конкурсе...", bot);
                break;
            case "Начать рассылку судьям":
                startJudgeBroadcast(chatId);
                break;
        }
    }

    private void resetContest(long chatId) {
        // Логика сброса конкурса
        BotUtils.sendMessage(chatId, "Конкурс сброшен: счётчик и список судей обнулены.", bot);
    }

    private void startJudgeBroadcast(long chatId) {
        // Логика рассылки судьям
        BotUtils.sendMessage(chatId, "Рассылка судьям начата.", bot);
    }
}
