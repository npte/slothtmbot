package org.slothmud.tmbot.auc.mapper;

import lombok.Builder;
import org.slothmud.tmbot.auc.model.AucLot;
import org.slothmud.tmbot.auc.model.Item;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StringToListAucLotMapperImpl implements StringToListAucLotMapper {

    static final String STRING_SEPARATOR = "\n";

    @Override
    public List<AucLot> map(String s) {
        return Optional.ofNullable(s)
                .map(e -> e.split(STRING_SEPARATOR))
                .map(e -> Arrays.stream(e)
                        .map(line -> {
                                    String[] tokens = line.split(" ");
                                    return AucListLine.builder()
                                            .slot(getToken(tokens, 0))
                                            .seller(getToken(tokens, 1))
                                            .bidder(getToken(tokens, 2))
                                            .vnum(intValue(getToken(tokens, 3)))
                                            .price(intValue(getToken(tokens, 4)))
                                            .buyout(intValue(getToken(tokens, 5)))
                                            .timeRemaining(intValue(getToken(tokens, 6)))
                                            .itemName(getRestTokens(tokens, 7))
                                            .source(line)
                                            .build();
                                }
                        )
                        .filter(aucListLine -> !aucListLine.seller.equalsIgnoreCase("-1"))
                        .map(aucListLine -> AucLot.builder()
                                .slot(aucListLine.slot)
                                .seller(aucListLine.seller)
                                .remainingTime(aucListLine.timeRemaining.toString())
                                .price(aucListLine.price)
                                .buyout(aucListLine.buyout)
                                .item(Item.builder()
                                        .vnum(aucListLine.vnum)
                                        .name(aucListLine.itemName)
                                        .build())
                                .source(aucListLine.source)
                                .build())
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    private Integer intValue(String token) {
        try {
            return Optional.ofNullable(token)
                    .map(Integer::parseInt)
                    .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    private String getToken(String[] tokens, int i) {
        if (tokens.length > i) {
            return tokens[i];
        }
        return null;
    }

    private String getRestTokens(String[] tokens, int i) {
        if (tokens.length > i) {
            return String.join(" ", Arrays.copyOfRange(tokens, i, tokens.length));
        }
        return null;
    }

    @Builder
    private static class AucListLine {
        private final String slot;
        private final String seller;
        private final String bidder;
        private final Integer vnum;
        private final Integer price;
        private final Integer buyout;
        private final Integer timeRemaining;
        private final String itemName;
        private final String source;
    }
}
