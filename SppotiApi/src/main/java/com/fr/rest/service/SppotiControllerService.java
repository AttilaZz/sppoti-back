package com.fr.rest.service;

import com.fr.commons.dto.SppotiRequestDTO;
import com.fr.commons.dto.SppotiResponseDTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by: Wail DJENANE on Jul 11, 2016
 */

@Service
public interface SppotiControllerService extends AbstractControllerService {


    /**
     * @param newSppoti     sppoti to save.
     * @return saved sppoti.
     */
    SppotiResponseDTO saveSppoti(SppotiRequestDTO newSppoti);

    /**
     * @param uuid sppoti unique id.
     * @return found sppoti.
     */
    SppotiResponseDTO getSppotiByUuid(Integer uuid);

    /**
     * @param id user id.
     * @return all sppoties created by a user.
     */
    List<SppotiResponseDTO> getAllUserSppoties(Integer id, int page);

    /**
     * @param id sppoti id.
     */
    void deleteSppoti(int id);

    /**
     *
     * @param sppotiRequest sppoti data to update.
     * @param id sppoti id.
     * @return sppoti DTO with updated data.
     */
    SppotiResponseDTO updateSppoti(SppotiRequestDTO sppotiRequest, int id);

    /**
     * ACCEPT sppoti and add notification.
     *
     * When user accept to join sppoti, he also accept to join his team.
     *
     * @param sppotiId sppoti id.
     * @param userId sppoter id.
     */
    void acceptSppoti(int sppotiId, int userId);

    /**
     * REFUSE sppoti and add notification.
     *
     * When user refuse sppoti, relation with his team remain.
     *
     * @param sppotiId sppoti id.
     * @param userId sppoter id.
     */
    void refuseSppoti(int sppotiId, int userId);

    /**
     *
     * @param sppotiId sppoti id to update.
     * @param adverseTeamResponseStatus adverse team response to challenge.
     * @return sppotiDTO with updated data.
     */
    SppotiResponseDTO updateTeamAdverseChallengeStatus(int sppotiId, int adverseTeamResponseStatus);
}
