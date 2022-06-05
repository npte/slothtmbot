package org.slothmud.tmbot.party.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slothmud.tmbot.party.model.PartyInfo;
import org.slothmud.tmbot.party.service.storage.ProcessedPartiesStorage;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class FilterOldPartiesService implements SlothMudPartiesService {

    private final SlothMudPartiesService slothMudPartiesService;
    private final ProcessedPartiesStorage processedPartiesStorage;

    @Override
    public List<PartyInfo> getParties() {
        List<PartyInfo> partyInfos = processedPartiesStorage.filterProcessedParties(slothMudPartiesService.getParties());

        log.debug("After filtering old parties: {} parties", partyInfos.size());

        return partyInfos;
    }

}
