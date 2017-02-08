package com.fr.rest.service;

import com.fr.commons.dto.SppotiRequest;
import com.fr.commons.dto.SppotiResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by: Wail DJENANE on Jul 11, 2016
 */

@Service
public interface SppotiControllerService extends AbstractControllerService {


    SppotiResponse saveSppoti(SppotiRequest newSppoti, Long sppotiCreator);

    SppotiResponse getSppotiByUuid(Integer uuid, Integer connectedUSer);

    List<SppotiResponse> getAllUserSppoties(Integer id, int page, Integer connectedUser);

    void deleteSppoti(int id);

    SppotiResponse updateSppoti(SppotiRequest sppotiRequest, int id, Integer connectedUser);

    void acceptSppoti(int sppotiId, int userId);

    void refuseSppoti(int sppotiId, int userId);
}
