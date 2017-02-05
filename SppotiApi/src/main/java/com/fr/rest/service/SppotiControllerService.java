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

    SppotiResponse getSppotiByUuid(int uuid);

    List<SppotiResponse> getAllUserSppoties(int id, int page);

    void deleteSppoti(int id);

    SppotiResponse updateSppoti(SppotiRequest sppotiRequest, int id);

    void acceptSppoti(int sppotiId, int userId);

    void refuseSppoti(int sppotiId, int userId);
}
