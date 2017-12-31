package com.fr.transformers.impl;

import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.utils.SppotiUtils;
import com.fr.entities.CommentEntity;
import com.fr.entities.UserEntity;
import com.fr.transformers.CommentTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by djenanewail on 2/11/17.
 */
@Component
public class CommentTransformerImpl extends AbstractTransformerImpl<CommentDTO, CommentEntity> implements
		CommentTransformer
{
	
	private final UserTransformerImpl userTransformer;
	
	@Autowired
	public CommentTransformerImpl(final UserTransformerImpl userTransformer)
	{
		this.userTransformer = userTransformer;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommentEntity dtoToModel(final CommentDTO dto)
	{
		return super.dtoToModel(dto);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommentDTO modelToDto(final CommentEntity model)
	{
		final CommentDTO commentDTO = new CommentDTO();
		final UserEntity userEntity = model.getUser();
		
		commentDTO.setAuthorId(userEntity.getUuid());
		commentDTO.setAuthorFirstName(userEntity.getFirstName());
		commentDTO.setAuthorLastName(userEntity.getLastName());
		commentDTO.setAuthorUsername(userEntity.getUsername());
		commentDTO.setId(model.getUuid());
		commentDTO.setAuthorUsername(userEntity.getUsername());
		commentDTO.setText(model.getContent());
		
		commentDTO.setImageLink(model.getImageLink());
		commentDTO.setVideoLink(model.getVideoLink());
		commentDTO.setCreationDate(SppotiUtils.dateWithTimeZone(model.getDatetimeCreated(), model.getTimeZone()));
		
		final UserDTO temp = this.userTransformer.getUserCoverAndAvatar(userEntity);
		commentDTO.setAuthorAvatar(temp.getAvatar());
		commentDTO.setAuthorCover(temp.getCover());
		
		return commentDTO;
	}
	
}
