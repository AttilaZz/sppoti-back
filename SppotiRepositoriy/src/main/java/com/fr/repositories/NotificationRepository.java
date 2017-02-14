package com.fr.repositories;

import com.fr.entities.NotificationEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by djenanewail on 12/13/16.
 */
public interface NotificationRepository extends JpaRepository <NotificationEntity, Long>{

    List<NotificationEntity> findByToUuidAndOpenedFalse(int userId, Pageable pageable);

    NotificationEntity findByUuid(int notifId);
}
