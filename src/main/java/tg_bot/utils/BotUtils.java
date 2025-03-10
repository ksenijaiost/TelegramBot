package tg_bot.utils;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg_bot.bot.AcChatBot;

import java.time.LocalDateTime;

import static tg_bot.constants.BotEnv.ADMIN_CHAT_ID;
import static tg_bot.constants.Buttons.*;

public class BotUtils {
    public void sendMessage(long chatId, String text, AcChatBot bot) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log("Ошибка при отправке сообщения: " + e.getMessage());
        }
    }

    public void sendNotMemberWarning(long chatId, AcChatBot bot) {
        String warning = "⚠️ Для использования этой функции необходимо быть участником нашего чата!\n" +
                "Присоединяйтесь: t.me/ваш_чат";
        sendMessage(chatId, warning, bot);
    }

    public void sendMessageWithKeyboard(long chatId, String text,
                                               ReplyKeyboardMarkup keyboardMarkup,
                                               AcChatBot bot) {
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

    public void sendHelp(long chatId, boolean isAdmin, AcChatBot bot) {
        String helpText;
        if (isAdmin) {
            helpText = "📋 *Команды для админа*:\n" +
                    "/reset - Сбросить конкурс\n" +
                    "/updateinfo - Обновить информацию о конкурсе\n" +
                    "Начать рассылку судьям - Выбрать судей и отправить им сообщения\n" +
                    "Конкурс - Управление конкурсом";
        } else {
            helpText = "📋 *Команды для пользователей*:\n" +
                    USER_GUIDES + " - Поиск гайдов по ключевым словам\n" +
                    USER_CONTEST + " - Узнать о конкурсе, записаться на участие или судейство\n" +
                    USER_TO_ADMIN + " - Отправить сообщения админам\n" +
                    USER_TO_NEWS + " - Отправить новости в газету чата\n" +
                    USER_TURNIP + " - Информация о скупке репы админами\n" +
                    USER_HEAD_CHAT + " - Ссылка на наш чат по Animal Crossing\n" +
                    USER_CHANEL + " - Ссылка на наш канал по Animal Crossing\n" +
                    USER_CHAT_NINTENDO + " - Ссылка на чат с темами по играм Nintendo, а также оффтоп-темой\n" ;
        }

        sendMessage(chatId, helpText, bot);
    }

    public void notifyAdmin(String message, AcChatBot bot) {
        sendMessage(Long.parseLong(ADMIN_CHAT_ID), "[Уведомление] " + message, bot);
    }

    public void log(String message) {
        System.out.println("[LOG] " + LocalDateTime.now() + " - " + message);
    }
}
