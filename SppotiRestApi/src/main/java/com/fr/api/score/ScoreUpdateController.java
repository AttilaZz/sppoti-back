package com.fr.api.score;

import com.fr.commons.dto.ScoreDTO;
import com.fr.service.ScoreControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by djenanewail on 4/7/17.
 */


@RestController
@RequestMapping("/score")
public class ScoreUpdateController {

    private final ScoreControllerService scoreControllerService;

    @Autowired
    public ScoreUpdateController(ScoreControllerService scoreControllerService) {
        this.scoreControllerService = scoreControllerService;
    }

    /**
     *
     * @param scoreDTO score data.
     * @return 202 status if updated.
     */
    @PutMapping("/{scoreId}")
    public ResponseEntity updateScoreStatusByAdverseTeam(@RequestBody ScoreDTO scoreDTO){
        scoreControllerService.updateScore(scoreDTO);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

}
