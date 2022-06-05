package org.slothmud.tmbot.config;

import org.slothmud.tmbot.message.sender.MessageSender;
import org.slothmud.tmbot.party.message.producer.PartyMessageProducer;
import org.slothmud.tmbot.party.message.producer.PartyMessageProducerImpl;
import org.slothmud.tmbot.party.message.sender.PartyMessageSender;
import org.slothmud.tmbot.party.service.FilterOldPartiesService;
import org.slothmud.tmbot.party.service.SlothMudPartiesInfoReceiver;
import org.slothmud.tmbot.party.service.SlothMudPartiesService;
import org.slothmud.tmbot.party.service.storage.FilterSmallPartiesService;
import org.slothmud.tmbot.party.service.storage.ProcessedPartiesStorageImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class SlothMudPartiesServiceConfig {

    @Bean
    public SlothMudPartiesService slothMudPartiesInfoReceiver(WebClient slothPartiesInfoWebClient) {
        return new SlothMudPartiesInfoReceiver(slothPartiesInfoWebClient);
    }

    @Bean
    public SlothMudPartiesService filterSmallPartiesService(SlothMudPartiesService slothMudPartiesInfoReceiver) {
        return new FilterSmallPartiesService(slothMudPartiesInfoReceiver);
    }

    @Bean
    public SlothMudPartiesService filterOldPartiesService(SlothMudPartiesService filterSmallPartiesService) {
        return new FilterOldPartiesService(filterSmallPartiesService, new ProcessedPartiesStorageImpl());
    }

    @Bean
    public PartyMessageProducer partyMessageProducer() {
        return new PartyMessageProducerImpl();
    }

    @Bean
    public MessageSender partyMessageSender(WebClient telegramWebClient,
                                            PartyMessageProducer partyMessageProducer,
                                            SlothMudPartiesService filterOldPartiesService,
                                            @Value("org.slothmud.tmbot.parties.chatId") String chatId) {
        return new PartyMessageSender(telegramWebClient, partyMessageProducer, filterOldPartiesService, chatId);
    }

}
