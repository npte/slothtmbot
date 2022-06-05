package org.slothmud.tmbot.auc.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slothmud.tmbot.auc.model.AucLot;
import org.slothmud.tmbot.auc.model.Item;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class EnrichFromEqListService implements WebAucListService {

    private final WebClient slothEqListWebClient;
    private final WebAucListService filterOldAuctionLotsService;

    @Override
    public List<AucLot> getAucItems() {
        return filterOldAuctionLotsService.getAucItems().stream()
                .map(this::getItemEnrichedFromEqList)
                .collect(Collectors.toList());
    }

    private AucLot getItemEnrichedFromEqList(AucLot aucLot) {
        String name = getNameWithoutArticle(aucLot.getItem().getName());

        String response = callEqList(name);

        return enrich(aucLot, convertStringToDocument(response));
    }

    private String getNameWithoutArticle(String name) {
        if (name.toLowerCase().startsWith("a ")) {
            return name.toLowerCase().replace("a ", "");
        }
        if (name.toLowerCase().startsWith("an ")) {
            return name.toLowerCase().replace("an ", "");
        }
        return name;
    }

    @SneakyThrows
    private AucLot enrich(AucLot aucLot, Document document) {
        Elements items = document.select(".item");

        Item item = aucLot.getItem();

        //попробуем тупо найти по VNUM
        Optional<Element> byVnum = items.stream()
                .filter(element -> element.select(".name")
                        .stream()
                        .anyMatch(e -> e.attributes().hasKey("title") &&
                                e.attributes().get("title").equalsIgnoreCase("VNUM: " + item.getVnum())))
                .findAny();

        if (byVnum.isPresent()) {
            return aucLot.toBuilder()
                    .item(enrich(item, byVnum.get()))
                    .build();
        }

        //По VNUM не нашли, возможно это кусок руны
        Optional<Element> byRuneName = items.stream()
                .filter(element -> element.select(".name")
                        .stream()
                        .anyMatch(e -> e.hasText() &&
                                e.text().toLowerCase().contains(getNameWithoutArticle(item.getName()) + " rune")))
                .findAny();

        if (byRuneName.isPresent()) {
            return aucLot.toBuilder()
                    .item(enrich(item, byRuneName.get()))
                    .build();
        }

        log.error("Failed enrich auc lot: {}", aucLot.getSource());
        return aucLot;
    }

    private Item enrich(Item item, Element element) {
        return item.toBuilder()
                .stat(element.select("div > div.cell.stats > div:nth-child(1)").text())
                .comment(createComment(element))
                .build();
    }

    private String createComment(Element element) {
        StringBuilder sb = new StringBuilder();

        Elements cpsFor = element.select(".cpsFor");
        if (!cpsFor.isEmpty()) {
            sb.append(cpsFor.select(".attachitem").text());
        }

        return sb.toString();
    }

    private Document convertStringToDocument(String htmlString) {
        return Jsoup.parse(htmlString);
    }

    private String callEqList(String name) {
        return slothEqListWebClient.get()
                .uri("/?search={name}", name)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
