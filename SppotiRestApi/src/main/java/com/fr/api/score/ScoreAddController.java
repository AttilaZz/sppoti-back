package com.fr.api.score;

import com.fr.commons.dto.ScoreDTO;
import com.fr.service.SppotiControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by djenanewail on 4/7/17.
 */

@RestController
@RequestMapping("/score")
public class ScoreAddController {

    private SppotiControllerService sppotiControllerService;

    @Autowired
    void setSppotiControllerService(SppotiControllerService sppotiControllerService) {
        this.sppotiControllerService = sppotiControllerService;
    }

    /**
     * Logger.
     */
    private Logger LOGGER = Logger.getLogger(ScoreAddController.class);

    /**
     * Add score to a sppoti.
     *
     * @param scoreDTO score to add.
     * @return zdded score.
     */
    @PostMapping("/score/add")
    ResponseEntity<ScoreDTO> addScore(@RequestBody @Valid ScoreDTO scoreDTO) {

        return new ResponseEntity<>(sppotiControllerService.addSppotiScore(scoreDTO), HttpStatus.CREATED);
    }
}
