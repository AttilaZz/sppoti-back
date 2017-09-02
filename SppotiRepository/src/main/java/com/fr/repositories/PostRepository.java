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
public interface PostRepository extends JpaRepository<PostEntity, Long>
{
	List<PostEntity> getByUuidAndDeletedFalse(String postId);
	
	/**
	 * Find all users's and user friends posts.
	 *
	 * @return list of {@link PostEntity}
	 */
	@SuppressWarnings("SpringElInspection")
	@Override
	@PostFilter("(filterObject.user.id == authentication.getPrincipal().getId() " +
			"|| filterObject.user.friends.contains(authentication.getPrincipal().getUserAsFriend()))")
	List<PostEntity> findAll();
	

	@PostFilter("filterObject.user.id == authentication.getPrincipal().getId()")
	List<PostEntity> getByUuidAndDeletedFalseOrderByDatetimeCreatedDesc(String postUuid);
	
	
	List<PostEntity> getByAlbumIsNotNullAndDeletedFalseAndUserUuidOrderByDatetimeCreatedDesc(String userId,
																							 Pageable pageable);
	
	List<PostEntity> getByVideoIsNotNullAndDeletedFalseAndUserUuidOrderByDatetimeCreatedDesc(String userId,
																							 Pageable pageable);
	
	@Query("SELECT p FROM PostEntity p WHERE (p.user.id = :userId OR p.targetUserProfile.uuid = :userUuid) " +
			"AND p.visibility IN (:visibility) AND p.deleted = false")
	List<PostEntity> getAllPosts(@Param("userUuid") String userIntId, @Param("userId") Long userLongId,
								 @Param("visibility") List visibility, Pageable pageable);
	
	List<PostEntity> findByDeletedFalseAndUserIdIn(List<Long> usersPostToReturn, Pageable pageable);
}