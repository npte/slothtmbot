package org.slothmud.tmbot.auc.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class Item {

    private final Integer vnum;
    private final String name;
    private final String stat;
    private final String comment;

}
