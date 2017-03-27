package com.fr.enums;

/**
 * Created by: Wail DJENANE on Oct 6, 2016
 */
public enum CoverType {

    IMAGE(1), VIDEO(2);

    private int type;

    CoverType(int type) {
        this.type = type;
    }

    public int type() {
        return this.type;
    }

    public String getName() {
        return this.name();
    }
}
