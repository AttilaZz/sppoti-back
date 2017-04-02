/**
 *
 */
package com.fr.commons.enumeration;

/**
 * Created by: Wail DJENANE on Aug 16, 2016
 */
public enum UserRoleTypeEnum {
    USER("USER"),
    ADMIN("ADMIN"),
    CLIENT("CLIENT");

    String userRoleType;

    private UserRoleTypeEnum(String userProfileType) {
        this.userRoleType = userProfileType;
    }

    public String getUserProfileType() {
        return userRoleType;
    }
}
