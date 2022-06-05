package org.slothmud.tmbot.party.message.sender;

import lombok.RequiredArgsConstructor;
import org.slothmud.tmbot.message.sender.MessageSender;
import org.slothmud.tmbot.party.message.producer.PartyMessageProducer;
import org.slothmud.tmbot.party.service.SlothMudPartiesService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class PartyMessageSender implements MessageSender {

    private final WebClient telegramWebClient;
    private final PartyMessageProducer partyMessageProducerImpl;
    private final SlothMudPartiesService slothMudPartiesService;
    private final String chatId;

    @Override
    @Scheduled(fixedRate = 15, timeUnit = TimeUnit.MINUTES)
    public void sendMessage() {
        slothMudPartiesService.getParties().stream()
                .map(partyMessageProducerImpl::produceMessage)
                .forEach(this::send);
    }

    private void send(String message) {
        telegramWebClient.get()
                .uri("/sendMessage?chat_id={chatId}&text={message}", chatId, message)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
