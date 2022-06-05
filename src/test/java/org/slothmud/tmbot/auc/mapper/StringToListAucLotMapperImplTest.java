package org.slothmud.tmbot.auc.mapper;

import org.junit.jupiter.api.Test;
import org.slothmud.tmbot.auc.model.AucLot;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.slothmud.tmbot.auc.mapper.StringToListAucLotMapperImpl.STRING_SEPARATOR;

class StringToListAucLotMapperImplTest {

    private static final String TEST_DATA = "AUC_27 Gnu -1 39847 200000 300000 13520 a straight edger" + STRING_SEPARATOR +
                    "AUC_28 Tau -1 712 5500000 6000000 5818 Blue Quartz Rune" + STRING_SEPARATOR +
                    "AUC_29 -1" + STRING_SEPARATOR +
                    "AUC_30 -1" + STRING_SEPARATOR +
                    "AUC_31 -1";

    private final StringToListAucLotMapperImpl mapper = new StringToListAucLotMapperImpl();

    @Test
    void map() {
        List<AucLot> aucLots = mapper.map(TEST_DATA);

        assertEquals(2, aucLots.size());
        Optional<AucLot> aucLotOptional = findBySeller(aucLots, "Gnu");
        assertTrue(aucLotOptional.isPresent());
        assertEquals(200000, aucLotOptional.get().getPrice());
        assertEquals(300000, aucLotOptional.get().getBuyout());
        assertEquals("13520", aucLotOptional.get().getRemainingTime());
        assertEquals("a straight edger", aucLotOptional.get().getItem().getName());

        aucLotOptional = findBySeller(aucLots, "Tau");
        assertTrue(aucLotOptional.isPresent());
        assertEquals(5500000, aucLotOptional.get().getPrice());
        assertEquals(6000000, aucLotOptional.get().getBuyout());
        assertEquals("5818", aucLotOptional.get().getRemainingTime());
        assertEquals("Blue Quartz Rune", aucLotOptional.get().getItem().getName());
    }

    private Optional<AucLot> findBySeller(List<AucLot> aucLots, String seller) {
        return aucLots.stream()
                .filter(aucLot -> seller.equalsIgnoreCase(aucLot.getSeller()))
                .findAny();
    }
}