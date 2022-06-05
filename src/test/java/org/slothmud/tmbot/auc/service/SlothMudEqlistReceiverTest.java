package org.slothmud.tmbot.auc.service;

import org.junit.jupiter.api.Test;
import org.slothmud.tmbot.auc.model.AucLot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class SlothMudEqlistReceiverTest {

    @Autowired
    private SlothMudEqlistReceiver slothMudEqlistReceiver;

    @Test
    void getAucItems() {
        List<AucLot> aucItems = slothMudEqlistReceiver.getAucItems();
        assertNotNull(aucItems);
    }

}