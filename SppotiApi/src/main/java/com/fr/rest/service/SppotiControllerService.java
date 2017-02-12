package com.fr.rest.service;

import com.fr.commons.dto.SppotiRequestDTO;
import com.fr.commons.dto.SppotiResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by: Wail DJENANE on Jul 11, 2016
 */

@Service
public interface SppotiControllerService extends AbstractControllerService {


    SppotiResponseDTO saveSppoti(SppotiRequestDTO newSppoti, Long sppotiCreator);

    SppotiResponseDTO getSppotiByUuid(Integer uuid, Integer connectedUSer);

    List<SppotiResponseDTO> getAllUserSppoties(Integer id, int page, Integer connectedUser);

    void deleteSppoti(int id);

    SppotiResponseDTO updateSppoti(SppotiRequestDTO sppotiRequest, int id, Integer connectedUser);

    void acceptSppoti(int sppotiId, int userId);

    void refuseSppoti(int sppotiId, int userId);
}
