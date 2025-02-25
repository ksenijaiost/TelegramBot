package tg_bot.functions;

import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg_bot.KeywordBot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static tg_bot.constants.BotEnv.CONTEST_CHAT_ID;

@AllArgsConstructor
public class ContestManager {
    private final KeywordBot bot;
    private LocalDate contestStartDate;
    private LocalDate contestEndDate;
    private int workCounter = 1;
    private Set<Long> judges = new HashSet<>();
    private Set<Long> participants = new HashSet<>();
    private Map<Long, String> judgeCodes = new HashMap<>();
    private Map<Long, Set<Long>> selectedJudges = new HashMap<>();
    private Map<Long, Boolean> waitingForGoogleFormLink = new HashMap<>();
    private String contestTheme = "Тема конкурса";
    private String contestAnnouncementLink = "https://example.com/announcement";
    private String contestRulesLink = "https://example.com/rules";

    public void handleContestCommand(long chatId, String text, Message message) {
        switch (text) {
            case "Конкурс":
                sendContestMenu(chatId);
                break;
            case "Узнать про конкурс":
                sendContestInfo(chatId);
                break;
            case "Отправить работу на конкурс":
                handleContestSubmission(chatId);
                break;
            case "Записаться на судейство":
                handleJudgeRegistration(chatId);
                break;
            case "Согласен":
                confirmJudgeRegistration(chatId);
                break;
            case "Отменить судейство":
                cancelJudgeRegistration(chatId);
                break;
        }
    }

    private void handleContestSubmission(long chatId) {
        if (isContestActive()) {
            BotUtils.sendMessage(chatId, "Отправьте до 10 фото и текст для вашей работы.", bot);
        } else {
            BotUtils.sendMessage(chatId, "Приём работ на конкурс закрыт. Следите за новостями!", bot);
        }
    }

    public void handleContestWorkSubmission(Message message) {
        long chatId = message.getChatId();

        if (isContestActive()) {
            StringBuilder contestMessage = new StringBuilder();
            contestMessage.append("Работа #").append(workCounter).append("\n");
            contestMessage.append("Отправитель: @").append(message.getFrom().getUserName()).append("\n");
            contestMessage.append("Текст: ").append(message.getCaption()).append("\n");

            try {
                for (int i = 0; i < Math.min(message.getPhoto().size(), 10); i++) {
                    String fileId = message.getPhoto().get(i).getFileId();
                    InputFile photo = new InputFile(fileId);
                    bot.execute(new SendPhoto(CONTEST_CHAT_ID, photo));
                }
                bot.execute(new SendMessage(CONTEST_CHAT_ID, contestMessage.toString()));
            } catch (TelegramApiException e) {
                BotUtils.log("Ошибка при отправке работы: " + e.getMessage());
            }

            BotUtils.sendMessage(chatId, "Ваша работа принята! Номер работы: #" + workCounter, bot);
            workCounter++;
        } else {
            BotUtils.sendMessage(chatId, "Приём работ на конкурс закрыт. Следите за новостями!", bot);
        }
    }

    public void handleDefaultMessage(long chatId, String text) {
        if (text.equalsIgnoreCase("назад")) {
            sendMainMenu(chatId);
        } else if (text.matches("\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}\\.\\d{2}\\.\\d{4}")) {
            if (BotUtils.isAdmin(chatId)) {
                String[] dates = text.split(" ");
                setContestPeriod(dates[0], dates[1], chatId);
            } else {
                BotUtils.sendMessage(chatId, "У вас нет прав для этой команды.", bot);
            }
        } else if (waitingForGoogleFormLink.getOrDefault(chatId, false)) {
            handleGoogleFormLink(chatId, text);
        } else {
            String response = getResponseByKeyword(text);
            BotUtils.sendMessage(chatId, response, bot);
        }
    }

    public void handleCallbackQuery(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
        String callbackData = callbackQuery.getData();

        if (callbackData.startsWith("select_judge_")) {
            long judgeId = Long.parseLong(callbackData.replace("select_judge_", ""));
            selectJudge(chatId, judgeId);
        } else if (callbackData.startsWith("unselect_judge_")) {
            long judgeId = Long.parseLong(callbackData.replace("unselect_judge_", ""));
            unselectJudge(chatId, judgeId);
        } else if (callbackData.equals("confirm_broadcast")) {
            confirmBroadcast(chatId);
        } else if (callbackData.equals("back_to_judge_selection")) {
            showJudgeSelection(chatId);
        }
    }

    private void sendContestMenu(long chatId) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("Узнать про конкурс");
        row1.add("Отправить работу на конкурс");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("Записаться на судейство");
        row2.add("Назад");

        keyboard.add(row1);
        keyboard.add(row2);

        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Меню конкурса:");
        message.setReplyMarkup(keyboardMarkup);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            BotUtils.log("Ошибка при отправке меню конкурса: " + e.getMessage());
        }
    }

    private void sendContestInfo(long chatId) {
        String contestInfo = String.format(
                "🎉 *Тема конкурса*: %s\n" +
                        "📅 *Дата проведения*: с %s по %s\n" +
                        "🔗 *Анонс*: %s\n" +
                        "📜 *Правила*: %s",
                contestTheme, contestStartDate, contestEndDate, contestAnnouncementLink, contestRulesLink
        );

        BotUtils.sendMessage(chatId, contestInfo, bot);
    }

    private void handleJudgeRegistration(long chatId) {
        if (participants.contains(chatId)) {
            BotUtils.sendMessage(chatId, "Вы не можете судить, так как уже участвуете в конкурсе.", bot);
            sendContestMenu(chatId);
        } else if (judges.contains(chatId)) {
            BotUtils.sendMessage(chatId, "Вы уже записаны на судейство.", bot);
            sendContestMenu(chatId);
        } else {
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
            List<KeyboardRow> keyboard = new ArrayList<>();

            KeyboardRow row1 = new KeyboardRow();
            row1.add("Согласен");
            row1.add("Назад к конкурсам");
            row1.add("В главное меню");

            keyboard.add(row1);

            keyboardMarkup.setKeyboard(keyboard);
            keyboardMarkup.setResizeKeyboard(true);

            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Вы согласны стать судьёй?");
            message.setReplyMarkup(keyboardMarkup);

            try {
                bot.execute(message);
            } catch (TelegramApiException e) {
                BotUtils.log("Ошибка при отправке сообщения: " + e.getMessage());
            }
        }
    }

    private void confirmJudgeRegistration(long chatId) {
        judges.add(chatId);
        BotUtils.sendMessage(chatId, "Вы успешно записаны на судейство!", bot);
        BotUtils.notifyAdmin("Пользователь " + chatId + " записался на судейство.", bot);
        sendContestMenu(chatId);
    }

    private void cancelJudgeRegistration(long chatId) {
        judges.remove(chatId);
        BotUtils.sendMessage(chatId, "Вы отменили запись на судейство.", bot);
        sendContestMenu(chatId);
    }

    private void showJudgeSelection(long chatId) {
        if (judges.isEmpty()) {
            BotUtils.sendMessage(chatId, "Нет записавшихся судей.", bot);
            return;
        }

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        for (Long judge : judges) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            if (selectedJudges.getOrDefault(chatId, new HashSet<>()).contains(judge)) {
                button.setText("❌ Судья " + judge);
                button.setCallbackData("unselect_judge_" + judge);
            } else {
                button.setText("✅ Судья " + judge);
                button.setCallbackData("select_judge_" + judge);
            }
            keyboard.add(Collections.singletonList(button));
        }

        InlineKeyboardButton confirmButton = new InlineKeyboardButton();
        confirmButton.setText("Подтвердить выбор");
        confirmButton.setCallbackData("confirm_broadcast");

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("Назад к выбору");
        backButton.setCallbackData("back_to_judge_selection");

        keyboard.add(Collections.singletonList(confirmButton));
        keyboard.add(Collections.singletonList(backButton));

        keyboardMarkup.setKeyboard(keyboard);

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите судей для рассылки:");
        message.setReplyMarkup(keyboardMarkup);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            BotUtils.log("Ошибка при отправке сообщения: " + e.getMessage());
        }
    }

    private void selectJudge(long chatId, long judgeId) {
        selectedJudges.computeIfAbsent(chatId, k -> new HashSet<>()).add(judgeId);
        showJudgeSelection(chatId);
    }

    private void unselectJudge(long chatId, long judgeId) {
        selectedJudges.computeIfAbsent(chatId, k -> new HashSet<>()).remove(judgeId);
        showJudgeSelection(chatId);
    }

    private void confirmBroadcast(long chatId) {
        Set<Long> selected = selectedJudges.get(chatId);
        if (selected == null || selected.isEmpty()) {
            BotUtils.sendMessage(chatId, "Не выбрано ни одного судьи.", bot);
            return;
        }

        BotUtils.sendMessage(chatId, "Введите ссылку на Google Forms:", bot);
        waitingForGoogleFormLink.put(chatId, true);
    }

    private void handleGoogleFormLink(long chatId, String text) {
        String googleFormLink = text;

        for (Long judge : selectedJudges.get(chatId)) {
            String code = UUID.randomUUID().toString();
            judgeCodes.put(judge, code);
        }

        for (Long judge : selectedJudges.get(chatId)) {
            String messageText = String.format(
                    "Привет! Мы решили провести небольшой эксперимент в судействе конкурсов: мы приглашаем некоторых активных участников чата, которые не являются участниками, тоже посудить!\n" +
                            "Почувствовать себя крутым судьёй и сделать свой вклад в победу участников можно тут: %s\n" +
                            "ВНИМАНИЕ!\n" +
                            "1) Не забудь ознакомиться с информацией на 1 странице\n" +
                            "2) В одном из вопросов нужно будет вставить код, он индивидуальный и нужен для защиты от \"левых\" судей (если ссылка попала к тому, к кому не должна была).\n" +
                            "Твой код (копируется тыком) - `%s`",
                    googleFormLink, judgeCodes.get(judge)
            );

            BotUtils.sendMessage(judge, messageText, bot);
        }

        BotUtils.sendMessage(chatId, "Рассылка выбранным судьям завершена.", bot);
        selectedJudges.remove(chatId);
        waitingForGoogleFormLink.put(chatId, false);
    }

    private boolean isContestActive() {
        LocalDate today = LocalDate.now();
        return contestStartDate != null && contestEndDate != null &&
                !today.isBefore(contestStartDate) && !today.isAfter(contestEndDate);
    }

    private void setContestPeriod(String startDateStr, String endDateStr, long chatId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        try {
            contestStartDate = LocalDate.parse(startDateStr, formatter);
            contestEndDate = LocalDate.parse(endDateStr, formatter);
            BotUtils.sendMessage(chatId, "Период приёма работ установлен с " + contestStartDate + " по " + contestEndDate, bot);
        } catch (DateTimeParseException e) {
            BotUtils.sendMessage(chatId, "Неверный формат даты. Используйте формат дд.мм.гггг.", bot);
        }
    }

    private void sendMainMenu(long chatId) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("Найти гайд");
        row1.add("Конкурс");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("Отправить предложение для админов");
        row2.add("Отправить информацию для газеты");

        keyboard.add(row1);
        keyboard.add(row2);

        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Главное меню:");
        message.setReplyMarkup(keyboardMarkup);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            BotUtils.log("Ошибка при отправке главного меню: " + e.getMessage());
        }
    }

    private String getResponseByKeyword(String text) {
        switch (text.toLowerCase()) {
            case "java":
                return "Вот ссылка на документацию по Java: https://docs.oracle.com/javase/8/docs/api/";
            case "spring":
                return "Вот ссылка на документацию по Spring: https://spring.io/projects/spring-framework";
            default:
                return "Извините, я не знаю такого ключевого слова.";
        }
    }
}