package com.fr.repositories;

import com.fr.entities.CommentEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostFilter;

import java.util.List;

/**
 * Created by djenanewail on 12/14/16.
 */
public interface CommentRepository extends JpaRepository<CommentEntity, Long>{

    CommentEntity getByUuid(int id);

    @PostFilter("!filterObject.isDeleted()")
    List<CommentEntity> getByPostUuidOrderByDatetimeCreatedDesc(int postId, Pageable pageable);

}
