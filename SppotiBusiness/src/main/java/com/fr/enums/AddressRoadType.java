package com.fr.enums;

/**
 * Created by: Wail DJENANE on Sep 20, 2016
 */
enum AddressRoadType {
    RUE("Rue"), AVENUE("Avenue");

    private String type;

    private AddressRoadType(final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }

    public String getName() {
        return this.name();
    }
}
