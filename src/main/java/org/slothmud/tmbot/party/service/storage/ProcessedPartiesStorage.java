package org.slothmud.tmbot.party.service.storage;

import org.slothmud.tmbot.party.model.PartyInfo;

import java.util.List;

public interface ProcessedPartiesStorage {

    List<PartyInfo> filterProcessedParties(List<PartyInfo> unfiltered);

}
