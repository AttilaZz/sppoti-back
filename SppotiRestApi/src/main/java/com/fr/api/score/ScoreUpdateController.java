package com.fr.api.score;

import com.fr.commons.dto.ScoreDTO;
import com.fr.security.AccountUserDetails;
import com.fr.service.ScoreControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by djenanewail on 4/7/17.
 */


@RestController
@RequestMapping("/score")
public class ScoreUpdateController
{
	
	/** Score controller service. */
	private final ScoreControllerService scoreControllerService;
	
	/** Init score service. */
	@Autowired
	public ScoreUpdateController(ScoreControllerService scoreControllerService)
	{
		this.scoreControllerService = scoreControllerService;
	}
	
	/**
	 * @param scoreDTO
	 * 		score data.
	 *
	 * @return 202 status if updated.
	 */
	@PutMapping
	public ResponseEntity updateScoreStatusByAdverseTeam(@RequestBody ScoreDTO scoreDTO, Authentication authentication)
	{
		AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();
		
		scoreControllerService.updateScore(scoreDTO, accountUserDetails.getId());
		return new ResponseEntity(HttpStatus.ACCEPTED);
	}
	
}
