package tg_bot.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tg_bot.Bot;
import tg_bot.rest.dao.OneMsgRequest;
import tg_bot.rest.dao.SendingRequest;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

/**
 * Рест контроллер.
 */
@RestController
@RequestMapping(value = "/")
@RequiredArgsConstructor
@Slf4j
public class BotController {
    private final Bot bot;

    @PostMapping(value = "sending", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> sendingMsg(@RequestBody SendingRequest sendingRequest) {
        var answer = bot.sendMsg(sendingRequest.getList());
        return ResponseEntity.ok()
                .body(answer);
    }

    @PostMapping(value = "oneMsg", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> oneMsg(@RequestBody OneMsgRequest oneMsgRequest) {
        var answer = bot.oneMsg(oneMsgRequest.getChatIds(), oneMsgRequest.getText());
        return ResponseEntity.ok()
                .body(answer);
    }
}
