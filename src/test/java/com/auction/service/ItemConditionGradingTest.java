package com.auction.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemConditionGradingTest {

    @Test
    void testConditionParsing() {
        assertEquals(ItemConditionGrading.ConditionTier.MINT, ItemConditionGrading.parseGrade("MINT"));
        assertEquals(ItemConditionGrading.ConditionTier.NEAR_MINT, ItemConditionGrading.parseGrade("Near Mint"));
        assertEquals(ItemConditionGrading.ConditionTier.POOR, ItemConditionGrading.parseGrade("invalid"));
    }
}
