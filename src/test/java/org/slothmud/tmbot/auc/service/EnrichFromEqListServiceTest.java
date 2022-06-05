package org.slothmud.tmbot.auc.service;

import org.junit.jupiter.api.Test;
import org.slothmud.tmbot.auc.model.AucLot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class EnrichFromEqListServiceTest {

    @Autowired
    private EnrichFromEqListService enrichFromEqListService;

    @Test
    void getAucItems() {
        List<AucLot> aucItems = enrichFromEqListService.getAucItems();

    }
}