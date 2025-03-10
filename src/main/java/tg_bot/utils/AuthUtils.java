package tg_bot.utils;

import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg_bot.bot.AcChatBot;
import tg_bot.constants.BotEnv;

import static tg_bot.constants.BotEnv.CHAT_ID;

public class AuthUtils {
    public boolean isAdmin(long userId) {
        return BotEnv.getAdminIds().contains(userId);
    }

    public boolean isSuperAdmin(long userId) {
        return BotEnv.getSuperAdminId() == userId;
    }

    public boolean isChatMember(AcChatBot bot, long userId) {
        GetChatMember getChatMember = new GetChatMember();
        getChatMember.setChatId(CHAT_ID);
        getChatMember.setUserId(userId);

        try {
            ChatMember chatMember = bot.execute(getChatMember);
            String status = chatMember.getStatus().toLowerCase();
            return status.equals("member")
                    || status.equals("administrator")
                    || status.equals("creator");
        } catch (TelegramApiException e) {
            System.err.println("Ошибка проверки участника чата: " + e.getMessage());
            return false;
        }
    }
}
