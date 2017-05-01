/*
 * Créé le 14 févr. 2017 par tstrauss
 */
package com.fr.transformers;

import com.fr.commons.dto.AbstractCommonDTO;
import com.fr.entities.AbstractCommonEntity;

import java.util.List;

/**
 * Interface des transformer de l'application.
 *
 * @author tstrauss
 * @author $LastChangedBy${0xD}
 * @version $Revision$ $Date${0xD}
 */
public interface CommonTransformer<T extends AbstractCommonDTO,E extends AbstractCommonEntity>
{
	/**
	 * Transformation d'un objet DTO en sont objet model.
	 *
	 * @param dto
	 * 		Le DTO.
	 *
	 * @return Le model correspondant.
	 */
	E dtoToModel(final T dto);
	
	/**
	 * Transformation d'un objet model en sont objet DTO.
	 *
	 * @param model
	 * 		Le model
	 *
	 * @return Le DTO correspondant.
	 */
	T modelToDto(final E model);
	
	/**
	 * Transformation d'une {@link List} d'objets DTO en une {@link List} de leurs
	 * objets model.
	 *
	 * @param dtos
	 * 		La {@link List} d'objets DTO.
	 *
	 * @return La {@link List} d'objet models correspondant.
	 */
	List<E> dtoToModel(final List<T> dtos);
	
	/**
	 * Transformation d'une {@link List} d'objets model en une {@link List} de
	 * leurs objets DTO.
	 *
	 * @param models
	 * 		La {@link List} d'objets model
	 *
	 * @return La {@link List} d'objet DTO correspondant.
	 */
	List<T> modelToDto(final List<E> models);
	
}
