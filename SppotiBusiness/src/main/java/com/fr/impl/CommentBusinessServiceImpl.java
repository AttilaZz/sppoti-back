package com.fr.impl;

import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.ContentEditedResponseDTO;
import com.fr.commons.dto.EmailUserDTO;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.entities.CommentEntity;
import com.fr.entities.EditHistoryEntity;
import com.fr.entities.PostEntity;
import com.fr.entities.UserEntity;
import com.fr.service.CommentBusinessService;
import com.fr.service.NotificationBusinessService;
import com.fr.service.email.CommentMailerService;
import com.fr.transformers.CommentTransformer;
import com.fr.transformers.PostTransformer;
import com.fr.transformers.UserTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.fr.commons.enumeration.notification.NotificationObjectType.COMMENT;
import static com.fr.commons.enumeration.notification.NotificationTypeEnum.X_COMMENTED_ON_YOUR_POST;

/**
 * Created by: Wail DJENANE on Aug 12, 2016
 */
@Component
class CommentBusinessServiceImpl extends CommonControllerServiceImpl implements CommentBusinessService
{
	
	private final Logger LOGGER = LoggerFactory.getLogger(CommentBusinessServiceImpl.class);
	
	@Autowired
	private CommentMailerService commentMailerService;
	@Autowired
	private CommentTransformer commentTransformer;
	@Autowired
	private NotificationBusinessService notificationService;
	@Autowired
	private UserTransformer userTransformer;
	@Autowired
	private PostTransformer postTransformer;
	
	@Value("${key.commentsPerPage}")
	private int commentSize;
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public CommentDTO saveComment(final CommentDTO dto)
	{
		final CommentEntity commentEntityToSave = new CommentEntity();
		commentEntityToSave.setTimeZone(dto.getTimeZone());
		
		final String content = dto.getText();
		final String image = dto.getImageLink();
		final String video = dto.getVideoLink();
		final String postId = dto.getPostId();
		boolean checkForTags = false;
		
		if (StringUtils.isEmpty(content) && StringUtils.isEmpty(image) && StringUtils.isEmpty(video)) {
			throw new BusinessGlobalException("No content has been specified in this comment");
		}
		
		if (StringUtils.hasText(content)) {
			checkForTags = true;
			commentEntityToSave.setContent(content);
		}
		
		if (StringUtils.hasText(image)) {
			commentEntityToSave.setImageLink(image);
		}
		
		if (StringUtils.hasText(video)) {
			commentEntityToSave.setVideoLink(video);
		}
		
		final Optional<PostEntity> optional = this.postRepository.findTopByUuidAndDeletedFalse(postId);
		
		optional.orElseThrow(() -> new EntityNotFoundException("Post id not found in database, data are corrupt"));
		
		commentEntityToSave.setPost(optional.get());
		
		commentEntityToSave.setUser(getConnectedUser());
		
		this.LOGGER.info("The following comment is going to be saved: {}", commentEntityToSave);
		final CommentEntity commentEntity = this.commentRepository.saveAndFlush(commentEntityToSave);
		final CommentDTO commentDTO = this.commentTransformer.modelToDto(commentEntity);
		
		final String targetUser = commentEntity.getPost().getTargetUserProfile().getUuid();
		if (StringUtils.hasText(targetUser) && !Objects.equals(targetUser, getConnectedUser().getUuid())) {
			
			//like on other posts not mine
			if (!Objects.equals(commentEntity.getUser().getUuid(),
					commentEntity.getPost().getTargetUserProfile().getUuid())) {
				
				this.notificationService.saveAndSendNotificationToUsers(getConnectedUser(),
						commentEntity.getPost().getTargetUserProfile(), COMMENT, X_COMMENTED_ON_YOUR_POST,
						commentEntity.getPost(), commentEntity);
				
				this.commentMailerService
						.sendEmailToPostContributors(this.postTransformer.modelToDto(optional.get()), commentDTO,
								buildContributorsList(optional.get()));
			}
			
			if (checkForTags) {
				this.notificationService.checkForTagNotification(null, commentEntity);
			}
		}
		
		commentDTO.setMyComment(true);
		
		this.LOGGER.info("Comment has been saved and the following DTO has been returned to the user: {}", commentDTO);
		return commentDTO;
	}
	
	private List<EmailUserDTO> buildContributorsList(final PostEntity postEntity) {
		final List<UserEntity> contributorsList = new ArrayList<>();
		contributorsList.add(postEntity.getUser());
		postEntity.getCommentEntities().forEach(c -> contributorsList.add(c.getUser()));
		
		return this.userTransformer.modelToDto(contributorsList).stream()
				.map(u -> new EmailUserDTO(u.getFirstName(), u.getLastName(), u.getEmail(), u.getId()))
				.filter(distinctByKey(EmailUserDTO::getEmail)).collect(Collectors.toList());
	}
	
	public static <T> Predicate<T> distinctByKey(final Function<? super T, ?> keyExtractor) {
		final Set<Object> seen = ConcurrentHashMap.newKeySet();
		return t -> seen.add(keyExtractor.apply(t));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public boolean deleteComment(final CommentEntity commentEntity)
	{
		commentEntity.setDeleted(true);
		try {
			this.commentRepository.save(commentEntity);
			return true;
		} catch (final Exception e) {
			this.LOGGER.error("Error saving commentEntity: ", e);
			return false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommentEntity findComment(final String id)
	{
		return this.commentRepository.getByUuidAndDeletedFalse(id);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public boolean updateComment(final EditHistoryEntity commentToEdit)
	{
		
		try {
			this.editHistoryRepository.save(commentToEdit);
			return true;
		} catch (final Exception e) {
			this.LOGGER.error("Error saving edited like: ", e);
			return false;
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CommentDTO> getPostCommentsFromLastId(final String postId, final int page, final Long userId)
	{
		
		final Pageable pageable = new PageRequest(page, this.commentSize);
		
		final List<CommentEntity> lCommentEntity = this.commentRepository
				.getByPostUuidAndDeletedFalseOrderByDatetimeCreatedDesc(postId, pageable);
		
		//userId used to distinguich connected user comments
		return fillCommentModelList(lCommentEntity, userId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ContentEditedResponseDTO> getAllCommentHistory(final String id, final int page)
	{
		
		final Pageable pageable = new PageRequest(page, this.commentSize);
		
		final List<EditHistoryEntity> dsHistoryList = this.editHistoryRepository
				.getByCommentUuidOrderByDatetimeEditedDesc(id, pageable);
		return fillEditContentResponse(dsHistoryList);
	}
	
}
