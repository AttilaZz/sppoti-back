package com.fr.transformers.impl;

import com.fr.commons.dto.AbstractCommonDTO;
import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.commons.utils.SppotiBeanUtils;
import com.fr.entities.AbstractCommonEntity;
import com.fr.transformers.CommonTransformer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract transformer class.
 * <p>
 * Created by wdjenane on 30/03/2017.
 */
@Transactional(readOnly = true)
public abstract class AbstractTransformerImpl<T extends AbstractCommonDTO, E extends AbstractCommonEntity> implements
		CommonTransformer<T, E>
{
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public List<E> dtoToModel(final List<T> dtos)
	{
		final List<E> models = new ArrayList<E>();
		if ((dtos != null) && !dtos.isEmpty()) {
			dtos.forEach(dto -> models.add(this.dtoToModel(dto)));
		}
		return models;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public List<T> modelToDto(final List<E> models)
	{
		final List<T> dtos = new ArrayList<T>();
		if ((models != null) && !models.isEmpty()) {
			models.forEach(model -> dtos.add(this.modelToDto(model)));
		}
		return dtos;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public E dtoToModel(final T dto)
	{
		final ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		final String className = type.getActualTypeArguments()[1].getTypeName();
		
		final E model;
		
		try {
			@SuppressWarnings("unchecked") final Class<E> clazz = (Class<E>) Class.forName(className);
			model = clazz.newInstance();
		} catch (final ClassNotFoundException | InstantiationException | IllegalAccessException exception) {
			throw new BusinessGlobalException("Erreur lors de l'instantiation de l'entité Java par réflexion.");
		}
		if (dto != null) {
			SppotiBeanUtils.copyProperties(dto, model);
		}
		return model;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public T modelToDto(final E model)
	{
		final ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		final String className = type.getActualTypeArguments()[0].getTypeName();
		
		final T dto;
		try {
			@SuppressWarnings("unchecked") final Class<T> clazz = (Class<T>) Class.forName(className);
			dto = clazz.newInstance();
		} catch (final ClassNotFoundException | InstantiationException | IllegalAccessException exception) {
			throw new BusinessGlobalException("Erreur lors de l'instantiation du dto Java par réflexion.");
		}
		
		if (model != null) {
			SppotiBeanUtils.copyProperties(model, dto);
		}
		return dto;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public List<T> iterableModelsToDtos(final Iterable<E> models) {
		return null;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public String getTimeZone() {
		final AccountUserDetails accountUserDetails = (AccountUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return accountUserDetails.getTimeZone();
	}
}
