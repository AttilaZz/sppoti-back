package com.fr.controllers.service;

import com.fr.models.SppotiRequest;
import org.springframework.stereotype.Service;

/**
 * Created by: Wail DJENANE on Jul 11, 2016
 */

@Service
public interface SppotiControllerService extends AbstractControllerService {


    void saveSppoti(SppotiRequest newSppoti, Long sppotiCreator);
}
