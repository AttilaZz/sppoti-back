package com.fr.service;

/**
 * Created by djenanewail on 11/10/17.
 */
public interface UserParamService
{
	boolean canReceiveEmail(String userId);
	
	boolean canReceiveNotification(String userId);
}
