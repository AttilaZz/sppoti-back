/**
 *
 */
package com.fr.enums;

/**
 * Created by: Wail DJENANE on Oct 6, 2016
 */
public enum CoverType {

    IMAGE(0), VIDEO(1);

    private int type;

    private CoverType(int type) {
        this.type = type;
    }

    public int type() {
        return this.type;
    }

    public String getName() {
        return this.name();
    }
}
