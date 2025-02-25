package tg_bot;

import lombok.AllArgsConstructor;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import tg_bot.functions.AdminManager;
import tg_bot.functions.BotUtils;
import tg_bot.functions.ContestManager;
import tg_bot.functions.GuideManager;

import static tg_bot.constants.BotEnv.*;

@AllArgsConstructor
public class KeywordBot extends TelegramLongPollingBot {
    private final ContestManager contestManager;
    private final GuideManager guideManager;
    private final AdminManager adminManager;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();

            if (text != null) {
                switch (text) {
                    case "Найти гайд":
                    case "Показать список":
                    case "Найти по ключевому слову":
                    case "Конкурс":
                    case "Узнать про конкурс":
                    case "Отправить работу на конкурс":
                    case "Записаться на судейство":
                    case "Согласен":
                    case "Отменить судейство":
                        contestManager.handleContestCommand(chatId, text, update.getMessage());
                        break;
                    case "/reset":
                    case "/updateinfo":
                    case "Начать рассылку судьям":
                        adminManager.handleAdminCommand(chatId, text, update.getMessage());
                        break;
                    case "/help":
                        BotUtils.sendHelp(chatId, isAdmin(chatId), this);
                        break;
                    default:
                        contestManager.handleDefaultMessage(chatId, text);
                }
            } else if (update.getMessage().hasPhoto()) {
                contestManager.handleContestWorkSubmission(update.getMessage());
            }
        } else if (update.hasCallbackQuery()) {
            contestManager.handleCallbackQuery(update.getCallbackQuery());
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    private boolean isAdmin(long chatId) {
        return String.valueOf(chatId).equals(ADMIN_CHAT_ID);
    }
}

