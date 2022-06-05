package org.slothmud.tmbot.auc.service;

import lombok.RequiredArgsConstructor;
import org.slothmud.tmbot.auc.model.AucLot;
import org.slothmud.tmbot.auc.service.storage.ProcessedAuctionLotsStorage;

import java.util.List;

@RequiredArgsConstructor
public class FilterOldAuctionLotsService implements WebAucListService {

    private final WebAucListService slothMudEqlistReceiver;
    private final ProcessedAuctionLotsStorage processedAuctionLotsStorage;

    @Override
    public List<AucLot> getAucItems() {
        return processedAuctionLotsStorage.filterProcessedAuctionLots(
                slothMudEqlistReceiver.getAucItems()
        );
    }

}
