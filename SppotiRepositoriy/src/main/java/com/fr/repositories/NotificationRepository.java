package com.fr.repositories;

import com.fr.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by djenanewail on 12/13/16.
 */
public interface NotificationRepository extends JpaRepository <Notification, Long>{

}
