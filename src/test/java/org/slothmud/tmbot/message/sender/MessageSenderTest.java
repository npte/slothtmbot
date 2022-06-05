package org.slothmud.tmbot.message.sender;

import org.junit.jupiter.api.Test;
import org.slothmud.tmbot.auc.message.sender.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MessageSenderTest {

    @Autowired
    private MessageSender messageSender;

    @Test
    void sendMessage() {
        messageSender.sendMessage();
    }
}