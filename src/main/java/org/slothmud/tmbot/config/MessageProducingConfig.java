package org.slothmud.tmbot.config;

import org.slothmud.tmbot.auc.message.producer.MessageProducer;
import org.slothmud.tmbot.auc.message.producer.MessageProducerImpl;
import org.slothmud.tmbot.auc.message.sender.MessageSender;
import org.slothmud.tmbot.auc.service.WebAucListService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageProducingConfig {

    @Bean
    public MessageProducer messageProducer() {
        return new MessageProducerImpl();
    }

    @Bean
    public MessageSender messageSender(WebAucListService enrichFromEqListService,
                                       MessageProducer messageProducer) {
        return new MessageSender(enrichFromEqListService, messageProducer);
    }

}
