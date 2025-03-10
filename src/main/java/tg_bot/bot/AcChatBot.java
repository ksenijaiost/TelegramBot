package tg_bot.bot;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg_bot.service.AdminManager;
import tg_bot.service.ContestManager;
import tg_bot.service.GuideManager;
import tg_bot.utils.AuthUtils;
import tg_bot.utils.BotUtils;

import java.util.ArrayList;
import java.util.List;

import static tg_bot.constants.BotEnv.BOT_NAME;
import static tg_bot.constants.Buttons.*;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class AcChatBot extends TelegramLongPollingBot {
    private final BotUtils botUtils = new BotUtils();
    private final AuthUtils authUtils = new AuthUtils();
    private final ContestManager contestManager = new ContestManager(this, botUtils, authUtils);
    private final GuideManager guideManager = new GuideManager();
    private final AdminManager adminManager = new AdminManager(this, botUtils, authUtils);

    private void sendMainMenu(long chatId) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = getKeyboardRows();

        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберите раздел:");
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private static List<KeyboardRow> getKeyboardRows() {
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первый ряд
        KeyboardRow row1 = new KeyboardRow();
        row1.add(USER_GUIDES);
        row1.add(USER_CONTEST);

        // Второй ряд
        KeyboardRow row2 = new KeyboardRow();
        row2.add(USER_TO_ADMIN);
        row2.add(USER_TO_NEWS);

        // Третий ряд
        KeyboardRow row3 = new KeyboardRow();
        row3.add(USER_TURNIP);
        row3.add(USER_HEAD_CHAT);

        // Четвертый ряд
        KeyboardRow row4 = new KeyboardRow();
        row4.add(USER_CHANEL);
        row4.add(USER_CHAT_NINTENDO);

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);
        return keyboard;
    }

    private void sendGuidesMenu(long chatId) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(USER_GUIDE_SITE);
        row1.add(USER_FIND_GUIDE);

        KeyboardRow row2 = new KeyboardRow();
        row2.add(USER_MENU);

        keyboard.add(row1);
        keyboard.add(row2);

        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Меню гайдов:");
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void sendContestsMenu(long chatId) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(USER_CONTEST_INFO);
        row1.add(USER_CONTEST_SEND);

        KeyboardRow row2 = new KeyboardRow();
        row2.add(USER_CONTEST_JUDGE);
        row2.add(USER_MENU);

        keyboard.add(row1);
        keyboard.add(row2);

        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Меню конкурсов:");
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();

            if (text != null) {
                switch (text) {
                    case USER_MENU:
                        sendMainMenu(chatId);
                        break;
                    case USER_GUIDES:
                        sendGuidesMenu(chatId);
                        break;
                    case USER_CONTEST:
                        sendContestsMenu(chatId);
                        break;
                    case USER_TO_ADMIN:
                    case USER_TO_NEWS:
                        if (authUtils.isChatMember(this, chatId)) {
                            botUtils.sendMessage(chatId, "Напишите новость для газеты:", this);
                        } else {
                            botUtils.sendNotMemberWarning(chatId, this);
                        }
                        break;
                    case USER_GUIDE_SITE:
                    case USER_FIND_GUIDE:
                    case USER_CONTEST_INFO:
                    case USER_CONTEST_SEND:
                    case USER_CONTEST_JUDGE:
                    case "Согласен":
                    case "Отменить судейство":
                        contestManager.handleContestCommand(chatId, text, update.getMessage());
                        break;
                    case "Сбросить конкурс":
                    case "Обновить информацию о конкурсе":
                    case "Начать рассылку судьям":
                        adminManager.handleAdminCommand(chatId, text, update.getMessage());
                        break;
                    case "/help":
                        botUtils.sendHelp(chatId, authUtils.isAdmin(chatId), this);
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

    private void sendMessageWithLink(long chatId, String text, String url) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text + "\n" + url);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }
}

