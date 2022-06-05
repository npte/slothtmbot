package org.slothmud.tmbot.auc.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class AucLot {

    private final String slot;
    private final String seller;
    private final String remainingTime;
    private final Integer price;
    private final Integer buyout;
    private final Item item;
    private final String source;

}
