package com.fr.impl;

import com.fr.entities.SportEntity;
import com.fr.service.SportControllerService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by djenanewail on 1/22/17.
 */

@Component
class SportControllerServiceImpl extends AbstractControllerServiceImpl implements SportControllerService
{
	
	SportControllerServiceImpl() {
		super(this.messagingTemplate);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SportEntity> getAllSports()
	{
		return this.sportRepository.findAll();
	}
}
