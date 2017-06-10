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
	/**
	 * Get post by uuid.
	 *
	 * @param postId
	 * 		post id.
	 *
	 * @return posts data.
	 */
	List<PostEntity> getByUuidAndDeletedFalse(int postId);
	
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
	
	/**
	 * Get all user's posts.
	 *
	 * @param posiUuid
	 * 		post id.
	 *
	 * @return list of all user's posts.
	 */
	@PostFilter("filterObject.user.id == authentication.getPrincipal().getId()")
	List<PostEntity> getByUuidAndDeletedFalseOrderByDatetimeCreatedDesc(int posiUuid);
	
	/**
	 * Get all posts containing a picture.
	 *
	 * @param userId
	 * 		user id.
	 * @param pageable
	 * 		page number.
	 *
	 * @return lists of {@link PostEntity}
	 */
	List<PostEntity> getByAlbumIsNotNullAndDeletedFalseAndUserUuidOrderByDatetimeCreatedDesc(int userId,
																							 Pageable pageable);
	
	/**
	 * Get all videos posts.
	 *
	 * @param userId
	 * 		user id.
	 * @param pageable
	 * 		page number.
	 *
	 * @return list of posts containing a video.
	 */
	List<PostEntity> getByVideoIsNotNullAndDeletedFalseAndUserUuidOrderByDatetimeCreatedDesc(int userId,
																							 Pageable pageable);
	
	@Query("SELECT p FROM PostEntity p WHERE (p.user.id = :userId OR p.targetUserProfile.uuid = :userUuid) " +
			"AND p.visibility IN (:visibility) AND p.deleted = false")
	List<PostEntity> getAllPosts(@Param("userUuid") int userIntId, @Param("userId") Long userLongId,
								 @Param("visibility") List visibility, Pageable pageable);
	
	/**
	 * Get non deleted posts for a given user id.
	 *
	 * @param uuid
	 * 		user id.
	 * @param pageable
	 * 		page number.
	 *
	 * @return list of {@link PostEntity}
	 */
	List<PostEntity> getByUserUuidAndDeletedFalse(int uuid, Pageable pageable);
}