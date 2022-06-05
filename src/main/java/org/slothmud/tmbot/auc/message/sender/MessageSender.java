package org.slothmud.tmbot.auc.message.sender;

import lombok.RequiredArgsConstructor;
import org.slothmud.tmbot.auc.message.producer.MessageProducer;
import org.slothmud.tmbot.auc.service.WebAucListService;

@RequiredArgsConstructor
public class MessageSender implements org.slothmud.tmbot.message.sender.MessageSender {

    private final WebAucListService webAucListService;
    private final MessageProducer messageProducer;

    @Override
    public void sendMessage() {
        webAucListService.getAucItems().stream()
                .map(messageProducer::produceMessage)
                .forEach(System.out::println);
    }

}
