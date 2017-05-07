package com.fr.repositories;

import com.fr.entities.PostEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostFilter;

import java.util.List;

/**
 * Created by djenane wail on 12/11/16.
 */
@SuppressWarnings("ALL")
public interface PostRepository extends JpaRepository<PostEntity, Long>
{
	
	@PostFilter("!filterObject.isDeleted() ")
	List<PostEntity> getByUuid(int postId);
	
	@PostFilter(
			"(filterObject.user.id == authentication.getPrincipal().getId() || filterObject.user.friends.contains(authentication.getPrincipal().getUserAsFriend())) && !filterObject.isDeleted() ")
	List<PostEntity> findAll();
	
	@PostFilter("filterObject.user.id == authentication.getPrincipal().getId()")
	List<PostEntity> getByUuidAndDeletedFalseOrderByDatetimeCreatedDesc(int id, Pageable pageable);
	
	@PostFilter(
			"(filterObject.user.id == authentication.getPrincipal().getId() || filterObject.user.friends.contains(authentication.getPrincipal().getUserAsFriend())) && !filterObject.isDeleted() ")
	List<PostEntity> getByUserUuidOrderByDatetimeCreatedDesc(int uuid, Pageable pageable);
	
	List<PostEntity> getByAlbumIsNotNullAndDeletedFalseAndUserUuidOrderByDatetimeCreatedDesc(int userdId,
																							 Pageable pageable);
	
	List<PostEntity> getByVideoIsNotNullAndDeletedFalseAndUserUuidOrderByDatetimeCreatedDesc(int userId,
																							 Pageable pageable);
	
	@Query("SELECT p FROM PostEntity p WHERE (p.user.id = :userId OR p.targetUserProfileUuid = :userUuid) AND p.visibility IN (:visibility) AND p.deleted = false ")
	List<PostEntity> getAllPosts(@Param("userUuid") int userIntId, @Param("userId") Long userLongId,
								 @Param("visibility") List visibility, Pageable pageable);
	
	List<PostEntity> getByUserUuidAndDeletedFalse(int uuid, Pageable pageable);
}