package com.fr.repositories;

import com.fr.entities.Post;
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
public interface PostRepository extends JpaRepository<Post, Long> {

    @PostFilter("!filterObject.isDeleted() ")
    List<Post> getByUuid(int postId);

    @PostFilter("(filterObject.user.id == authentication.getPrincipal().getId() || filterObject.user.friends.contains(authentication.getPrincipal().getUserAsFriend())) && !filterObject.isDeleted() ")
    List<Post> findAll();

    @PostFilter("filterObject.user.id == authentication.getPrincipal().getId() && !filterObject.isDeleted()")
    List<Post> getByUuidOrderByDatetimeCreatedDesc(int id, Pageable pageable);

    @PostFilter("(filterObject.user.id == authentication.getPrincipal().getId() || filterObject.user.friends.contains(authentication.getPrincipal().getUserAsFriend())) && !filterObject.isDeleted() ")
    List<Post> getByUserUuidOrderByDatetimeCreatedDesc(int uuid, Pageable pageable);

    @PostFilter("filterObject.user.id == authentication.getPrincipal().getId() && !filterObject.isDeleted() ")
    List<Post> getByAlbumIsNotNullOrderByDatetimeCreatedDesc(Pageable pageable);

    @PostFilter("filterObject.user.id == authentication.getPrincipal().getId() && !filterObject.isDeleted() ")
    List<Post> getByVideoIsNotNullOrderByDatetimeCreatedDesc(Pageable pageable);

    @PostFilter("!filterObject.isDeleted() ")
    @Query("SELECT p FROM Post p WHERE (p.user.id = :userId OR p.targetUserProfileUuid = :userUuid) AND p.visibility IN (:visibility)")
    List<Post> getAllPosts(@Param("userUuid") int userIntId, @Param("userId") Long userLongId, @Param("visibility") List visibility, Pageable pageable);
}