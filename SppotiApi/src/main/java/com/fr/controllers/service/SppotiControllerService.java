package com.fr.controllers.service;

import com.fr.commons.dto.SppotiRequest;
import com.fr.commons.dto.SppotiResponse;
import org.springframework.stereotype.Service;

/**
 * Created by: Wail DJENANE on Jul 11, 2016
 */

@Service
public interface SppotiControllerService extends AbstractControllerService {


    SppotiResponse saveSppoti(SppotiRequest newSppoti, Long sppotiCreator);

    SppotiResponse getSppotiByUuid(int uuid);

}
