package com.fr.RepositoriesService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fr.pojos.Messages;

/**
 * Created by: Wail DJENANE on Jun 16, 2016
 */
@Service
public interface MessageDaoService extends GenericDaoService<Messages, Integer> {

    Messages getMessageById(Long id);

    List<Messages> getAllSentMessagesByUserId(Long id, int buttomMarker);

    List<Messages> getAllReceivedtMessagesByUserId(Long id, int buttomMarker);

}
