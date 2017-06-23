package com.fr.repositories;

import com.fr.entities.CommentEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by djenanewail on 12/14/16.
 */
public interface CommentRepository extends JpaRepository<CommentEntity, Long>{

    CommentEntity getByUuidAndDeletedFalse(String id);

    List<CommentEntity> getByPostUuidAndDeletedFalseOrderByDatetimeCreatedDesc(String postId, Pageable pageable);

}
