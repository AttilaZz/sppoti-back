package com.fr.repositories;

import com.fr.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by djenanewail on 12/14/16.
 */
public interface CommentRepository extends JpaRepository<Comment, Long>{

}
