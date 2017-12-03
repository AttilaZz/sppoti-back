package com.fr.repositories;

import com.fr.entities.LikeContentEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by djenanewail on 12/15/16.
 */
public interface LikeRepository extends JpaRepository<LikeContentEntity, Long>
{
	
	LikeContentEntity findTopByPostIdAndUserId(Long poostId, Long userId);
	
	List<LikeContentEntity> getByPostUuidOrderByDatetimeCreatedDesc(String id, Pageable pageable1);
	
	LikeContentEntity findTopByUserIdAndPostUuid(Long userId, String postId);
	
	LikeContentEntity findTopByCommentId(Long id);
	
	LikeContentEntity findTopByUserIdAndCommentUuid(Long userId, String commentId);
	
	List<LikeContentEntity> findByCommentUuidOrderByDatetimeCreatedDesc(String id, Pageable pageable1);
}