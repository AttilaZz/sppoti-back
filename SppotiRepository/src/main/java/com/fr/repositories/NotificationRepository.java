package com.fr.repositories;

import com.fr.entities.NotificationEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by djenanewail on 12/13/16.
 */
@Service
public interface NotificationRepository extends JpaRepository <NotificationEntity, Long>{

    List<NotificationEntity> findByToUuid(int userId, Pageable pageable);

    NotificationEntity findByUuid(int notifId);

    Integer countByToUuid(int userId);
}
