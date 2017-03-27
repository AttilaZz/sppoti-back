package com.fr.service;

import com.fr.commons.dto.SppotiRatingDTO;
import com.fr.commons.dto.sppoti.SppotiRequestDTO;
import com.fr.commons.dto.sppoti.SppotiResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by: Wail DJENANE on Jul 11, 2016
 */

@Service
public interface SppotiControllerService extends AbstractControllerService {


    /**
     * @param newSppoti sppoti to save.
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
     * @param sppotiRequest sppoti data to update.
     * @param id            sppoti id.
     * @return sppoti DTO with updated data.
     */
    SppotiResponseDTO updateSppoti(SppotiRequestDTO sppotiRequest, int id);

    /**
     * ACCEPT sppoti and add notification.
     * <p>
     * When user accept to join sppoti, he also accept to join his team.
     *
     * @param sppotiId sppoti id.
     * @param userId   sppoter id.
     */
    void acceptSppoti(int sppotiId, int userId);

    /**
     * REFUSE sppoti and add notification.
     * <p>
     * When user refuse sppoti, relation with his team remain.
     *
     * @param sppotiId sppoti id.
     * @param userId   sppoter id.
     */
    void refuseSppoti(int sppotiId, int userId);

    /**
     * @param sppotiId                  sppoti id to update.
     * @param adverseTeamResponseStatus adverse team response to challenge.
     * @return sppotiDTO with updated data.
     */
    SppotiResponseDTO updateTeamAdverseChallengeStatus(int sppotiId, int adverseTeamResponseStatus);

    /**
     * @param id   user id.
     * @param page page  umber.
     * @return all sppoties that user has joined.
     */
    List<SppotiResponseDTO> getAllJoinedSppoties(int id, int page);

    /**
     * @param userId   id of the connected user.
     * @param sppotiId id of the sppoti.
     */
    void isSppotiAdmin(int sppotiId, Long userId);

    /**
     * This method allow to challenge a team in a sppoti.
     *  @param sppotiId sppoti id.
     * @param teamId   team id.
     */
    SppotiResponseDTO sendChallenge(int sppotiId, int teamId, Long connectedUserId);

    /**
     * rating many sppoters at a time.
     *
     * @param sppotiRatingDTO list of sppoters to rate.
     * @param sppotiId sppoti id.
     */
    void rateSppoters(List<SppotiRatingDTO> sppotiRatingDTO, int sppotiId);

    /**
     *
     * @param userId user id.
     * @param page page number.
     * @return all confirmed sppoties.
     */
    List<SppotiResponseDTO> getAllConfirmedSppoties(int userId, int page);

    /**
     *
     * @param userId user id.
     * @param page page number.
     * @return all refused sppoties.
     */
    List<SppotiResponseDTO> getAllRefusedSppoties(int userId, int page);
}
