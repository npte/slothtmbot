package org.slothmud.tmbot.auc.message.producer;

import org.slothmud.tmbot.auc.model.AucLot;

public interface MessageProducer {

    String produceMessage(AucLot aucLot);

}
