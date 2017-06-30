
package com.enviosya.reviewread.entities;

public enum ReviewFeeling {
    NEUTRAL,
    POSITIVE,
    NEGATIVE;
    
    public static ReviewFeeling fromInteger(int x) {
        switch(x) {
        case 0:
            return NEUTRAL;
        case 1:
            return POSITIVE;
        case 2:
            return NEGATIVE;
        }
        return null;
    }
}

