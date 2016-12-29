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

    Post getByUuid(int postId);

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

    @Query("select p from Post p where  p.visibility = :visibility and p.sport.id IN(:sports)")
    List<Post> getAllPosts(@Param("sports") List userSport, @Param("visibility") int visibility, Pageable pageable);
}
