
package com.enviosya.reviewwrite.entities;


public enum ReviewStatus {
    PENDING,
    APPROVED,
    REJECTED;
    
    public static ReviewStatus fromInteger(int x) {
        switch(x) {
        case 0:
            return PENDING;
        case 1:
            return APPROVED;
        case 2:
            return REJECTED;
        }
        return null;
    }
}
