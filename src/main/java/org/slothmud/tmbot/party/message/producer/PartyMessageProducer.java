package org.slothmud.tmbot.party.message.producer;

import org.slothmud.tmbot.party.model.PartyInfo;

public interface PartyMessageProducer {

    String produceMessage(PartyInfo partyInfo);

}
