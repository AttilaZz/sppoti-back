package com.fr.repositories;

import com.fr.entities.LikeContent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

/**
 * Created by djenanewail on 12/15/16.
 */
public interface LikeRepository extends CrudRepository<LikeContent, Long> {

    LikeContent getByPostId(Long id);

    Page findAll(Pageable pageable);

    List<LikeContent> getByPostUuidOrderByDatetimeCreated(int id, Pageable pageable1);

    LikeContent getByUserIdAndPostUuid(Long userId, int postId);
}
