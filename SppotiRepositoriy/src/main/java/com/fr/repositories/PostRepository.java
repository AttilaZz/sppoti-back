package com.fr.repositories;

import com.fr.entities.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostFilter;

import java.util.List;

/**
 * Created by djenane wail on 12/11/16.
 */
public interface PostRepository extends JpaRepository<Post, Long> {

    @PostFilter("filterObject.user.id == authentication.getPrincipal().getId() && !filterObject.isDeleted()")
    List<Post> getById(Long postId);

    @PostFilter("(filterObject.user.id == authentication.getPrincipal().getId() || filterObject.user.friends.contains(authentication.getPrincipal().getUserAsFriend())) && !filterObject.isDeleted() ")
    List<Post> findAll();

    @PostFilter("filterObject.user.id == authentication.getPrincipal().getId() && !filterObject.isDeleted()")
    List<Post> getByUuid(int id);

    @PostFilter("(filterObject.user.id == authentication.getPrincipal().getId() || filterObject.user.friends.contains(authentication.getPrincipal().getUserAsFriend())) && !filterObject.isDeleted() ")
    List<Post> getByUserUuid(int uuid, Pageable pageable);
}
