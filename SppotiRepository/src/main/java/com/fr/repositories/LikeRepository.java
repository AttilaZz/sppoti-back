package com.fr.repositories;

import com.fr.entities.LikeContentEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by djenanewail on 12/15/16.
 */
public interface LikeRepository extends CrudRepository<LikeContentEntity, Long>
{
	
	LikeContentEntity findByPostIdAndUserId(Long poostId, Long userId);
	
	List<LikeContentEntity> getByPostUuidOrderByDatetimeCreatedDesc(String id, Pageable pageable1);
	
	LikeContentEntity getByUserIdAndPostUuid(Long userId, String postId);
	
	LikeContentEntity getByCommentId(Long id);
	
	Object getByUserIdAndCommentUuid(Long userId, String commentId);
	
	List<LikeContentEntity> getByCommentUuidOrderByDatetimeCreatedDesc(String id, Pageable pageable1);
}
