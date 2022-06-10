package org.slothmud.tmbot.party.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slothmud.tmbot.party.model.PartyInfo;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class SlothMudPartiesInfoReceiver implements SlothMudPartiesService {

    private final WebClient slothPartiesInfoWebClient;

    @Override
    public List<PartyInfo> getParties() {
        String content = receivePartiesInfoPageContent();

        if (content == null) return Collections.emptyList();

        List<PartyInfo> partyInfos = extractPartiesInfo(Jsoup.parse(content));

        log.debug("Total received {} parties", partyInfos.size());

        return partyInfos;
    }

    private List<PartyInfo> extractPartiesInfo(Document document) {
        Elements elements = document.select("#post-29 > div > div.art-post-inner.art-article > div.art-postcontent > center > table");

        if (elements.size() == 0) {
            return Collections.emptyList();
        }

        Element table = elements.get(0);

        List<String> rows = table.select("tbody > tr").stream()
                .map(Element::text)
                .filter(e -> !e.isEmpty())
                .filter(e -> !e.startsWith("Last Updated:"))
                .collect(Collectors.toList());


        List<PartyInfo> response = new ArrayList<>();

        PartyInfo partyInfo = null;
        int groupSize = 0;
        for (String row : rows) {
            if (row.contains("is leading")) {
                if (partyInfo != null) {
                    partyInfo.setSize(groupSize);
                    response.add(partyInfo);
                    groupSize = 0;
                }
                partyInfo = new PartyInfo(row.substring(0, row.indexOf("is leading")));
            } else {
                groupSize++;
            }
        }

        if (partyInfo != null) {
            partyInfo.setSize(groupSize);
            response.add(partyInfo);
        }

        return  response;
    }

    private String receivePartiesInfoPageContent() {
        try {
            return slothPartiesInfoWebClient.get()
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.error("Error white receiving sloth party page", e);
        }
        return null;
    }
}
