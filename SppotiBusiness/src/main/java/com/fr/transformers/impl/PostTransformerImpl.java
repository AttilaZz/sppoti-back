package com.fr.transformers.impl;

import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.post.PostDTO;
import com.fr.commons.utils.SppotiUtils;
import com.fr.entities.*;
import com.fr.repositories.EditHistoryRepository;
import com.fr.repositories.UserRepository;
import com.fr.transformers.CommentTransformer;
import com.fr.transformers.PostTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Created by djenanewail on 6/10/17.
 */
@Component
public class PostTransformerImpl extends AbstractTransformerImpl<PostDTO, PostEntity> implements PostTransformer
{
	/** history repo. */
	private final EditHistoryRepository editHistoryRepository;
	
	/** Comment transformer. */
	private final CommentTransformer commentTransformer;
	
	/** User repository. */
	private final UserRepository userRepository;
	
	/**
	 * Init transformer.
	 */
	@Autowired
	public PostTransformerImpl(final EditHistoryRepository editHistoryRepository,
							   final CommentTransformer commentTransformer, final UserRepository userRepository)
	{
		this.editHistoryRepository = editHistoryRepository;
		this.commentTransformer = commentTransformer;
		this.userRepository = userRepository;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PostEntity dtoToModel(final PostDTO dto) {
		return super.dtoToModel(dto);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PostDTO modelToDto(final PostEntity model) {
		
		final PostDTO pres = new PostDTO();
		
		final UserEntity postEditor = model.getUser();
		final Long connectedUser = model.getConnectedUserId();
		
		pres.setId(model.getUuid());
		pres.setContent(model.getContent());
		pres.setImageLink(model.getAlbum());
		pres.setVideoLink(model.getVideo());
		pres.setVisibility(model.getVisibility());
		
		pres.setFirstName(postEditor.getFirstName());
		pres.setLastName(postEditor.getLastName());
		pres.setUsername(postEditor.getUsername());
		
		pres.setSportId(model.getSport().getId());
		
		pres.setMyPost(connectedUser.equals(model.getUser().getId()));
		
		/*
		 * Check if content has been modified or not
		 */
		final List<EditHistoryEntity> editHistory = this.editHistoryRepository
				.getByPostUuidOrderByDatetimeEditedDesc(model.getUuid());
		
		if (!editHistory.isEmpty()) {
			pres.setEdited(true);
			final EditHistoryEntity ec = editHistory.get(0);
			pres.setDatetimeCreated(SppotiUtils.dateWithTimeZone(ec.getDatetimeEdited(), model.getTimeZone()));
			if (ec.getText() != null) {
				pres.setContent(ec.getText());
			}
			
			if (ec.getSport() != null) {
				final Long spId = ec.getSport().getId();
				pres.setSportId(spId);
			}
		} else {
			// post has not been edited - set initial params
			if (model.getContent() != null) {
				pres.setContent(model.getContent());
			}
			if (model.getSport() != null && model.getSport().getId() != null) {
				pres.setSportId(model.getSport().getId());
			}
			pres.setDatetimeCreated(SppotiUtils.dateWithTimeZone(model.getDatetimeCreated(), model.getTimeZone()));
		}

		/*
		 * Manage commentEntities count + last like
		 */
		final Set<CommentEntity> commentEntities = new TreeSet<>();
		commentEntities.addAll(model.getCommentEntities());
		pres.setCommentsCount(commentEntities.size());
		
		final List<CommentEntity> commentsListTemp = new ArrayList<>();
		commentsListTemp.addAll(commentEntities);
		
		final List<CommentDTO> commentList = new ArrayList<>();
		if (!commentsListTemp.isEmpty()) {
			final CommentEntity commentEntity = commentsListTemp.get(commentEntities.size() - 1);
			
			final CommentDTO commentModelDTO = this.commentTransformer.modelToDto(commentEntity);
			commentModelDTO.setMyComment(commentEntity.getUser().getId().equals(connectedUser));
			commentModelDTO.setLikeCount(commentEntity.getLikes().size());
			
			commentModelDTO.setLikedByUser(
					commentEntity.getLikes().stream().anyMatch(c -> c.getUser().getId().equals(connectedUser)));
			
			commentList.add(commentModelDTO);
		}
		pres.setComment(commentList);
		
		/*
		 * Has connected user liked this post or not.
		 */
		pres.setLikeCount(model.getLikes().size());
		pres.setLikedByUser(model.getLikes().stream().anyMatch(l -> l.getUser().getId().equals(connectedUser)));
		
		/*
		 * Set post editor avatar and cover.
		 */
		final List<ResourcesEntity> resources = new ArrayList<>();
		resources.addAll(postEditor.getResources());
		if (!resources.isEmpty()) {
			if (resources.get(0) != null && resources.get(0).getType() == 1) {
				pres.setAvatar(resources.get(0).getUrl());
			} else if (resources.size() > 1 && resources.get(1) != null && resources.get(1).getType() == 1) {
				pres.setAvatar(resources.get(1).getUrl());
			}
		}

		/*
		 * Check if post has been posted on a friend profile -- default value for integer is ZERO (UUID can never be a zero)
		 */
		
		final UserEntity t = model.getTargetUserProfile();
		pres.setTargetUser(t.getFirstName(), t.getLastName(), t.getUsername(), t.getUuid(),
				connectedUser.equals(t.getId()));
		
		
		return pres;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PostEntity> dtoToModel(final List<PostDTO> dtos) {
		return super.dtoToModel(dtos);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PostDTO> modelToDto(final List<PostEntity> models) {
		return models.stream().map(this::modelToDto).collect(Collectors.toList());
	}
}
