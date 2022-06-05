package org.slothmud.tmbot.party.message.producer;

import org.slothmud.tmbot.party.model.PartyInfo;

public class PartyMessageProducerImpl implements PartyMessageProducer {

    @Override
    public String produceMessage(PartyInfo partyInfo) {
        return partyInfo.getLeader() + " is leading group of " + partyInfo.getSize() + " members";
    }

}
