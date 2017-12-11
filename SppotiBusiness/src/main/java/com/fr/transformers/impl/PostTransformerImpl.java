package com.fr.transformers.impl;

import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.post.PostDTO;
import com.fr.commons.utils.SppotiUtils;
import com.fr.entities.CommentEntity;
import com.fr.entities.EditHistoryEntity;
import com.fr.entities.PostEntity;
import com.fr.repositories.EditHistoryRepository;
import com.fr.service.FriendBusinessService;
import com.fr.transformers.CommentTransformer;
import com.fr.transformers.PostTransformer;
import com.fr.transformers.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by djenanewail on 6/10/17.
 */
@Component
public class PostTransformerImpl extends AbstractTransformerImpl<PostDTO, PostEntity> implements PostTransformer
{
	@Autowired
	private EditHistoryRepository editHistoryRepository;
	@Autowired
	private CommentTransformer commentTransformer;
	@Autowired
	private UserTransformer userTransformer;
	@Autowired
	private FriendBusinessService friendBusinessService;
	
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
		
		final PostDTO post = new PostDTO();
		
		final Long connectedUser = model.getConnectedUserId();
		
		post.setId(model.getUuid());
		post.setContent(model.getContent());
		post.setImageLink(model.getAlbum());
		post.setVideoLink(model.getVideo());
		post.setVisibility(model.getVisibility());
		
		post.setSender(this.userTransformer.modelToDto(model.getUser()));
		
		post.setSportId(model.getSport().getId());
		
		post.setMyPost(connectedUser.equals(model.getUser().getId()));
		
		final List<EditHistoryEntity> postEditHistory = this.editHistoryRepository
				.getByPostUuidOrderByDatetimeEditedDesc(model.getUuid());
		
		if (!postEditHistory.isEmpty()) {
			post.setEdited(true);
			final EditHistoryEntity ec = postEditHistory.get(0);
			post.setDatetimeCreated(SppotiUtils.dateWithTimeZone(ec.getDatetimeEdited(), model.getTimeZone()));
			if (ec.getText() != null) {
				post.setContent(ec.getText());
			}
			
			if (ec.getSport() != null) {
				final Long spId = ec.getSport().getId();
				post.setSportId(spId);
			}
		} else {
			// post has not been edited - set initial params
			if (model.getContent() != null) {
				post.setContent(model.getContent());
			}
			if (model.getSport() != null && model.getSport().getId() != null) {
				post.setSportId(model.getSport().getId());
			}
			post.setDatetimeCreated(SppotiUtils.dateWithTimeZone(model.getDatetimeCreated(), model.getTimeZone()));
		}

		/*
		 * Manage commentEntities count + last like
		 */
		final Set<CommentEntity> commentEntities = new TreeSet<>();
		commentEntities.addAll(model.getCommentEntities());
		post.setCommentsCount(commentEntities.size());
		
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
		post.setComment(commentList);
		
		post.setLikeCount(model.getLikes().size());
		
		post.setLikedByUser(model.getLikes().stream().anyMatch(l -> l.getUser().getId().equals(connectedUser)));
		
		if (Objects.nonNull(model.getTargetUserProfile())) {
			post.setTargetUser(this.userTransformer.modelToDto(model.getTargetUserProfile()));
			post.setFriendShipStatus(
					this.friendBusinessService.getFriendShipStatus(model.getTargetUserProfile().getUuid()));
		}
		
		return post;
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
