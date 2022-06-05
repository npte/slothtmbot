package org.slothmud.tmbot.auc.mapper;

import org.slothmud.tmbot.auc.model.AucLot;

import java.util.List;

public interface StringToListAucLotMapper {

    List<AucLot> map(String s);

}
