package org.slothmud.tmbot.auc.message.producer;


import org.slothmud.tmbot.auc.model.AucLot;

public class MessageProducerImpl implements MessageProducer {

    @Override
    public String produceMessage(AucLot aucLot) {
        return "New auction lot  " +
                System.lineSeparator() +
                "##  " +
                aucLot.getItem().getName() +
                "  " +
                System.lineSeparator() +
                "Price: " +
                aucLot.getPrice() +
                System.lineSeparator() +
                System.lineSeparator() +
                "Buyout: " +
                aucLot.getBuyout() +
                System.lineSeparator() +
                System.lineSeparator() +
                aucLot.getItem().getStat() +
                System.lineSeparator() +
                System.lineSeparator() +
                aucLot.getItem().getComment();
    }

}
