package com.fr.commons.enumeration;

/**
 * Created by djenanewail on 12/26/16.
 */
public enum GlobalAppStatusEnum
{
	
	PUBLIC_RELATION(1), PENDING_SENT(2), PENDING(3), CONFIRMED(4), REFUSED(5), NO_CHALLENGE_YET(6), DELETED(9),
	CANCELED(10), LEFT(11), CANCELLED(12);
	
	private final int status;
	
	GlobalAppStatusEnum(final int status)
	{
		this.status = status;
	}
	
	public int getValue()
	{
		return this.status;
	}
	
	public boolean isNotPendingAndNotRefusedAndNotDeletedAndNotCancelled() {
		return !this.equals(PENDING) && !this.equals(REFUSED) && !this.equals(DELETED) && !this.equals(CANCELED);
	}
	
	public boolean isNotCancelledAndNotDeletedAndNotRefused() {
		return !this.equals(CANCELED) && !this.equals(DELETED) && !this.equals(REFUSED);
	}
	
	public boolean isConfirmed() {
		return this.equals(CONFIRMED);
	}
}

