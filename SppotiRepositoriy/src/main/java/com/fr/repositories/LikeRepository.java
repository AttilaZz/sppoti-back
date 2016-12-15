package com.fr.repositories;

import com.fr.entities.LikeContent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by djenanewail on 12/15/16.
 */
public interface LikeRepository extends CrudRepository<LikeContent, Long> {

    LikeRepository getByUserIdAndPostId(Long userId, Long postId);

    LikeContent getByPostId(Long id);

    Page findAll(Pageable pageable);

    List<LikeContent> getByPostIdOrderByDatetimeCreated(Long id, Pageable pageable);
}
