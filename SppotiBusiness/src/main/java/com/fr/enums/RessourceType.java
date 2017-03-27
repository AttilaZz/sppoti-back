/**
 *
 */
package com.fr.enums;

/**
 * Created by: Wail DJENANE on Oct 6, 2016
 */
public enum RessourceType {

    AVATAR(0), COVER(1);

    private int type;

    private RessourceType(int type) {
        this.type = type;
    }

    public int getResourceType() {
        return this.type;
    }

    public String getName() {
        return this.name();
    }

}
