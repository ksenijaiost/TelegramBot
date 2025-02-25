package tg_bot.constants;

import io.github.cdimascio.dotenv.Dotenv;

public class BotEnv {
    private static final Dotenv dotenv = Dotenv.configure().load();

    public static final String BOT_TOKEN = dotenv.get("BOT_TOKEN");
    public static final String BOT_NAME = dotenv.get("BOT_NAME");
    public static final String ADMIN_CHAT_ID = dotenv.get("ADMIN_CHAT_ID");
    public static final String CONTEST_CHAT_ID = dotenv.get("CONTEST_CHAT_ID");
    public static final String NEWSPAPER_CHAT_ID = dotenv.get("NEWSPAPER_CHAT_ID");
}
