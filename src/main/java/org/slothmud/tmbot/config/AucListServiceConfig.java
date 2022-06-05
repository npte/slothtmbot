package org.slothmud.tmbot.config;

import org.slothmud.tmbot.auc.mapper.StringToListAucLotMapper;
import org.slothmud.tmbot.auc.mapper.StringToListAucLotMapperImpl;
import org.slothmud.tmbot.auc.service.EnrichFromEqListService;
import org.slothmud.tmbot.auc.service.FilterOldAuctionLotsService;
import org.slothmud.tmbot.auc.service.SlothMudEqlistReceiver;
import org.slothmud.tmbot.auc.service.WebAucListService;
import org.slothmud.tmbot.auc.service.storage.ProcessedAuctionLotsStorage;
import org.slothmud.tmbot.auc.service.storage.ProcessedAuctionLotsStorageImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AucListServiceConfig {

    @Bean
    public StringToListAucLotMapper stringToListAucLotMapper() {
        return new StringToListAucLotMapperImpl();
    }

    @Bean
    public WebAucListService slothMudEqlistReceiver(WebClient slothAucListWebClient,
                                                    StringToListAucLotMapper stringToListAucLotMapper) {
        return new SlothMudEqlistReceiver(
                slothAucListWebClient,
                stringToListAucLotMapper
        );
    }

    @Bean
    public ProcessedAuctionLotsStorage processedAuctionLotsStorage() {
        return new ProcessedAuctionLotsStorageImpl();
    }

    @Bean
    public WebAucListService filterOldAuctionLotsService(WebAucListService slothMudEqlistReceiver,
                                                         ProcessedAuctionLotsStorage processedAuctionLotsStorage) {
        return new FilterOldAuctionLotsService(
                slothMudEqlistReceiver,
                processedAuctionLotsStorage
        );
    }

    @Bean
    public WebAucListService enrichFromEqListService(WebClient slothEqListWebClient,
                                                     WebAucListService filterOldAuctionLotsService) {
        return new EnrichFromEqListService(
                slothEqListWebClient,
                filterOldAuctionLotsService
        );
    }
}
