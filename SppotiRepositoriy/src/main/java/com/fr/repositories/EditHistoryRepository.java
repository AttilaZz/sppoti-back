package com.fr.repositories;

import com.fr.entities.EditHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by djenanewail on 12/15/16.
 */
public interface EditHistoryRepository extends JpaRepository<EditHistory, Long> {

    List<EditHistory> getByPostIdOrderByIdDesc(Long postId);

    List<EditHistory> getByCommentIdOrderByIdDesc(Long commentId);

}
