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
	
	LikeContentEntity getByPostId(Long id);
	
	List<LikeContentEntity> getByPostUuidOrderByDatetimeCreatedDesc(int id, Pageable pageable1);
	
	LikeContentEntity getByUserIdAndPostUuid(Long userId, int postId);
	
	LikeContentEntity getByCommentId(Long id);
	
	Object getByUserIdAndCommentUuid(Long userId, int commentId);
	
	List<LikeContentEntity> getByCommentUuidOrderByDatetimeCreatedDesc(int id, Pageable pageable1);
}
