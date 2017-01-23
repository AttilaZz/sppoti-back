/**
 *
 */
package com.fr.models;

/**
 * Created by: Wail DJENANE on Aug 16, 2016
 */
public enum UserRoleType {
    USER("USER"),
    ADMIN("ADMIN"),
    CLIENT("CLIENT");

    String userRoleType;

    private UserRoleType(String userProfileType) {
        this.userRoleType = userProfileType;
    }

    public String getUserProfileType() {
        return userRoleType;
    }
}
