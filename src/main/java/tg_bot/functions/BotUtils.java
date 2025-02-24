package tg_bot.functions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg_bot.KeywordBot;

import java.time.LocalDateTime;

public class BotUtils {
    public static final String ADMIN_CHAT_ID = "админский_чат_id";
    public static final String CONTEST_CHAT_ID = "чат_конкурса_id";

    public static void sendMessage(long chatId, String text, KeywordBot bot) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log("Ошибка при отправке сообщения: " + e.getMessage());
        }
    }

    public static void sendMessageWithKeyboard(long chatId, String text, ReplyKeyboardMarkup keyboardMarkup, KeywordBot bot) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setReplyMarkup(keyboardMarkup);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log("Ошибка при отправке сообщения: " + e.getMessage());
        }
    }

    public static void sendHelp(long chatId, boolean isAdmin, KeywordBot bot) {
        String helpText;
        if (isAdmin) {
            helpText = "📋 *Команды для админа*:\n" +
                    "/reset - Сбросить конкурс\n" +
                    "/updateinfo - Обновить информацию о конкурсе\n" +
                    "Начать рассылку судьям - Выбрать судей и отправить им сообщения\n" +
                    "Конкурс - Управление конкурсом";
        } else {
            helpText = "📋 *Команды для пользователей*:\n" +
                    "Конкурс - Узнать о конкурсе и участвовать\n" +
                    "Найти гайд - Поиск гайдов по ключевым словам";
        }

        sendMessage(chatId, helpText, bot);
    }

    public static void notifyAdmin(String message, KeywordBot bot) {
        sendMessage(Long.parseLong(ADMIN_CHAT_ID), "[Уведомление] " + message, bot);
    }

    public static void log(String message) {
        System.out.println("[LOG] " + LocalDateTime.now() + " - " + message);
    }

    public static boolean isAdmin(long chatId) {
        return String.valueOf(chatId).equals(ADMIN_CHAT_ID);
    }
}
