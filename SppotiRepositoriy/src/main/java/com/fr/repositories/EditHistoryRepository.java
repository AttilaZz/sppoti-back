package com.fr.repositories;

import com.fr.entities.EditHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by djenanewail on 12/15/16.
 */
public interface EditHistoryRepository extends JpaRepository<EditHistory, Long> {

    List<EditHistory> getByPostUuidOrderByDatetimeEditedDesc(int postId);

    List<EditHistory> getByPostUuidOrderByDatetimeEditedDesc(int postId, Pageable pageable);

    List<EditHistory> getByCommentUuidOrderByDatetimeEditedDesc(int commentId);

    List<EditHistory> getByCommentUuidOrderByDatetimeEditedDesc(int commentId, Pageable pageable);

}
