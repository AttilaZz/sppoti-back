package com.fr.api.score;

import com.fr.commons.dto.ScoreDTO;
import com.fr.service.ScoreControllerService;
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

    /** Service des scores. */
    private ScoreControllerService scoreControllerService;

    /** Init score service. */
    @Autowired
    public void setScoreControllerService(ScoreControllerService scoreControllerService) {
        this.scoreControllerService = scoreControllerService;
    }

    /**
     * Add score to a sppoti.
     *
     * @param scoreDTO score to add.
     * @return added score.
     */
    @PostMapping
    ResponseEntity<ScoreDTO> addScore(@RequestBody @Valid ScoreDTO scoreDTO) {

        return new ResponseEntity<>(scoreControllerService.addSppotiScore(scoreDTO), HttpStatus.CREATED);
    }
}
