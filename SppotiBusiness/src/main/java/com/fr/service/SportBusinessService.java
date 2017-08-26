package com.fr.service;

import com.fr.entities.SportEntity;

import java.util.List;

/**
 * Created by djenanewail on 1/22/17.
 */

public interface SportBusinessService extends AbstractBusinessService
{
	
	List<SportEntity> getAllSports();
	
}