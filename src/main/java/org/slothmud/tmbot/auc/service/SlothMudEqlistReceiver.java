package org.slothmud.tmbot.auc.service;

import lombok.RequiredArgsConstructor;
import org.slothmud.tmbot.auc.mapper.StringToListAucLotMapper;
import org.slothmud.tmbot.auc.model.AucLot;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class SlothMudEqlistReceiver implements WebAucListService {

    private final WebClient slothAucListWebClient;
    private final StringToListAucLotMapper stringToListAucLotMapper;

    @Override
    public List<AucLot> getAucItems() {
        return Optional.ofNullable(
                slothAucListWebClient.get()
                            .accept(MediaType.ALL)
                            .exchangeToMono(clientResponse -> clientResponse.bodyToMono(String.class))
                            .block()
                )
                .map(stringToListAucLotMapper::map)
                .orElse(Collections.emptyList());
    }

}
