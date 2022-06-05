package org.slothmud.tmbot.auc.service.storage;

import org.junit.jupiter.api.Test;
import org.slothmud.tmbot.auc.model.AucLot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.slothmud.tmbot.auc.service.storage.ProcessedAuctionLotsStorageImpl.STORAGE_FILE_NAME;

class ProcessedAuctionLotsStorageImplTest {

    private final ProcessedAuctionLotsStorageImpl storage = new ProcessedAuctionLotsStorageImpl();

    @Test
    void testStorageSaved() throws IOException {
        String slot = UUID.randomUUID().toString();
        String seller = UUID.randomUUID().toString();

        List<AucLot> aucLots = Collections.singletonList(AucLot.builder()
                .slot(slot)
                .seller(seller)
                .build());

        List<AucLot> res = storage.filterProcessedAuctionLots(aucLots);

        assertEquals(1, res.size());
        assertEquals(slot, res.get(0).getSlot());
        assertEquals(seller, res.get(0).getSeller());

        Path path = Paths.get(STORAGE_FILE_NAME);
        assertTrue(Files.exists(path));
        assertTrue(Files.readAllLines(path).get(0).contains(slot));
        assertTrue(Files.readAllLines(path).get(0).contains(seller));
        Files.delete(path);
    }

    @Test
    void testFilter() throws IOException {
        String slot = UUID.randomUUID().toString();
        String seller = UUID.randomUUID().toString();

        List<AucLot> aucLots = Collections.singletonList(AucLot.builder()
                .slot(slot)
                .seller(seller)
                .build());

        List<AucLot> res = storage.filterProcessedAuctionLots(aucLots);

        assertEquals(1, res.size());
        assertEquals(slot, res.get(0).getSlot());
        assertEquals(seller, res.get(0).getSeller());

        res = storage.filterProcessedAuctionLots(aucLots);

        assertEquals(0, res.size());

        Path path = Paths.get(STORAGE_FILE_NAME);
        assertTrue(Files.exists(path));
        assertTrue(Files.readAllLines(path).get(0).contains(slot));
        assertTrue(Files.readAllLines(path).get(0).contains(seller));
        Files.delete(path);
    }

    @Test
    void testRestoreStorage() {
        storage.init();
    }

}