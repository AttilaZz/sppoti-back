package com.fr.impl;

import com.fr.entities.SportEntity;
import com.fr.service.SportBusinessService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by djenanewail on 1/22/17.
 */

@Component
class SportBusinessServiceImpl extends CommonControllerServiceImpl implements SportBusinessService
{
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SportEntity> getAllSports()
	{
		return this.sportRepository.findAll();
	}
}
