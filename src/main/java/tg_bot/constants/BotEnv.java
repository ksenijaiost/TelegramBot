package tg_bot.constants;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BotEnv {
    private static final Dotenv dotenv = Dotenv.configure().load();

    public static final String BOT_TOKEN = dotenv.get("BOT_TOKEN");
    public static final String BOT_NAME = dotenv.get("BOT_NAME");
    public static final String CHAT_ID = dotenv.get("CHAT_ID");
    public static final String ADMIN_CHAT_ID = dotenv.get("ADMIN_CHAT_ID");
    public static final String CONTEST_CHAT_ID = dotenv.get("CONTEST_CHAT_ID");
    public static final String NEWSPAPER_CHAT_ID = dotenv.get("NEWSPAPER_CHAT_ID");

    public static List<Long> getAdminIds() {
        String ids = dotenv.get("ADMIN_ID_LIST", "");
        return Arrays.stream(ids.split(","))
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    public static long getSuperAdminId() {
        return Long.parseLong(dotenv.get("SUPER_ADMIN_ID"));
    }
}
