package org.slothmud.tmbot.party.service.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slothmud.tmbot.party.model.PartyInfo;
import org.slothmud.tmbot.party.service.SlothMudPartiesService;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class FilterSmallPartiesService implements SlothMudPartiesService {

    @Value("${org.slothmud.tmbot.parties.group-size:5}")
    private Integer groupSize;

    private final SlothMudPartiesService slothMudPartiesService;

    @Override
    public List<PartyInfo> getParties() {
        List<PartyInfo> partyInfos = slothMudPartiesService.getParties().stream()
                .filter(partyInfo -> Optional.ofNullable(partyInfo.getSize())
                        .map(e -> Integer.compare(e, groupSize))
                        .map(e -> e > 0)
                        .orElse(false))
                .collect(Collectors.toList());


        log.debug("After filter small parties: {} parties", partyInfos.size());

        return partyInfos;
    }
}
