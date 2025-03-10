package tg_bot.service;

import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg_bot.bot.AcChatBot;
import tg_bot.utils.AuthUtils;
import tg_bot.utils.BotUtils;

@AllArgsConstructor
public class AdminManager {
    private final AcChatBot bot;
    private final BotUtils botUtils;
    private final AuthUtils authUtils;

    public void handleAdminCommand(long chatId, String text, Message message) {
        if (!authUtils.isAdmin(chatId)) {
            botUtils.sendMessage(chatId, "У вас нет прав для этой команды.", bot);
            return;
        }

        switch (text) {
            case "/reset":
                resetContest(chatId);
                break;
            case "/updateinfo":
                botUtils.sendMessage(chatId, "Введите данные о конкурсе...", bot);
                break;
            case "Начать рассылку судьям":
                startJudgeBroadcast(chatId);
                break;
        }
    }

    private void resetContest(long chatId) {
        // Логика сброса конкурса
        botUtils.sendMessage(chatId, "Конкурс сброшен: счётчик и список судей обнулены.", bot);
    }

    private void startJudgeBroadcast(long chatId) {
        // Логика рассылки судьям
        botUtils.sendMessage(chatId, "Рассылка судьям начата.", bot);
    }
}
