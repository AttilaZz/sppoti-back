/**
 *
 */
package com.fr.enums;

/**
 * Created by: Wail DJENANE on Oct 10, 2016
 */
public enum VisibilityRestriction {
    PUBLIC(0), FRIEND(1), ONLY_ME(2);

    private int visibility;

    private VisibilityRestriction(int visibility) {
        this.visibility = visibility;
    }

    public int getVisibility() {
        return visibility;
    }

    public String getName() {
        return this.name();
    }
}
