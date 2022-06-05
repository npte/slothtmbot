package org.slothmud.tmbot.party.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SlothMudPartiesInfoReceiverTest {

    @Autowired
    private SlothMudPartiesInfoReceiver slothMudPartiesInfoReceiver;

    @Test
    void test() {
        slothMudPartiesInfoReceiver.getParties();
    }

}