package com.fr.impl;

import com.fr.commons.dto.SportDTO;
import com.fr.service.SportBusinessService;
import com.fr.transformers.impl.SportTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by djenanewail on 1/22/17.
 */

@Component
class SportBusinessServiceImpl extends CommonControllerServiceImpl implements SportBusinessService
{
	@Autowired
	private SportTransformer sportTransformer;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SportDTO> getAllSports()
	{
		return this.sportTransformer.modelToDto(this.sportRepository.findAll());
	}
}
