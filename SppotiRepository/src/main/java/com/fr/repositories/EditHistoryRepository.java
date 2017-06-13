package com.fr.repositories;

import com.fr.entities.EditHistoryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by djenanewail on 12/15/16.
 */
public interface EditHistoryRepository extends JpaRepository<EditHistoryEntity, Long> {

    List<EditHistoryEntity> getByPostUuidOrderByDatetimeEditedDesc(String postId);

    List<EditHistoryEntity> getByPostUuidOrderByDatetimeEditedDesc(String postId, Pageable pageable);

    List<EditHistoryEntity> getByCommentUuidOrderByDatetimeEditedDesc(String commentId);

    List<EditHistoryEntity> getByCommentUuidOrderByDatetimeEditedDesc(String commentId, Pageable pageable);

}
