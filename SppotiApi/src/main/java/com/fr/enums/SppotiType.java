/**
 * 
 */
package com.fr.enums;

/**
 * Created by: Wail DJENANE on Sep 20, 2016
 */
public enum SppotiType {
	
	PUBLIC(1), PRIVATE(2);

	private int state;

	private SppotiType(final int type) {
		this.state = type;
	}

	public int getType() {
		return this.state;
	}

	public String getName() {
		return this.name();
	}
}
