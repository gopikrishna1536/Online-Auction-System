package com.auction.service;

/**
 * Standardized Collectible Item Condition Grading Validator.
 */
public class ItemConditionGrading {

    public enum ConditionTier {
        MINT(10),
        NEAR_MINT(8),
        EXCELLENT(6),
        GOOD(4),
        FAIR(2),
        POOR(1);

        public final int score;
        ConditionTier(int score) { this.score = score; }
    }

    public static ConditionTier parseGrade(String gradeString) {
        if (gradeString == null) return ConditionTier.POOR;
        try {
            return ConditionTier.valueOf(gradeString.trim().toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            return ConditionTier.POOR;
        }
    }
}
