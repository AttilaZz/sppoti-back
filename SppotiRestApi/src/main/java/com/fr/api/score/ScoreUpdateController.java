package com.fr.api.score;

import com.fr.commons.dto.ScoreDTO;
import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.service.ScoreControllerService;
import com.fr.versionning.ApiVersion;
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
@ApiVersion("1")
public class ScoreUpdateController
{
	
	/** Score controller service. */
	private final ScoreControllerService scoreControllerService;
	
	/** Init score service. */
	@Autowired
	public ScoreUpdateController(final ScoreControllerService scoreControllerService)
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
	public ResponseEntity updateScoreStatusByAdverseTeam(@RequestBody final ScoreDTO scoreDTO,
														 final Authentication authentication)
	{
		final AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();
		
		this.scoreControllerService.updateScore(scoreDTO, accountUserDetails.getId());
		return new ResponseEntity(HttpStatus.ACCEPTED);
	}
	
}
