# TelegramBot
Код для рассылки через телеграм-бот. На вход в рест запросе подается список данных для рассылки, бот через фейн в телеграм-апи кидает запросы, выводит логи и ответом отдает список id+имя+текст

Требуется локальный запуск (только код).
Через постман кидается рест запрос на локал-хост с требуемыми данными, например для localhost:8080/sending:
{
    "list": [
    		{
    		  "chat_id":"",
    		  "text":""
    		}
	]
}