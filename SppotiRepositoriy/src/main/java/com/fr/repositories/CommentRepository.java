package com.fr.repositories;

import com.fr.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostFilter;

import java.util.List;

/**
 * Created by djenanewail on 12/14/16.
 */
public interface CommentRepository extends JpaRepository<Comment, Long>{

    Comment getByUuid(int id);

    @PostFilter("!filterObject.isDeleted()")
    List<Comment> getByPostUuidOrderByDatetimeCreatedDesc(int postId, Pageable pageable);

}
