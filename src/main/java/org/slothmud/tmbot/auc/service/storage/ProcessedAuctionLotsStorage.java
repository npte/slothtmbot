package org.slothmud.tmbot.auc.service.storage;

import org.slothmud.tmbot.auc.model.AucLot;

import java.util.List;

public interface ProcessedAuctionLotsStorage {

   List<AucLot> filterProcessedAuctionLots(List<AucLot> unfiltered);

}
