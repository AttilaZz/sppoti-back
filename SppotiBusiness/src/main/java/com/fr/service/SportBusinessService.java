package com.fr.service;

import com.fr.commons.dto.SportDTO;

import java.util.List;

/**
 * Created by djenanewail on 1/22/17.
 */

public interface SportBusinessService extends AbstractBusinessService
{
	
	List<SportDTO> getAllSports();
	
}