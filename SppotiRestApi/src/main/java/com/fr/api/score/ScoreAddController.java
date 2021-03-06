package com.fr.api.score;

import com.fr.commons.dto.ScoreDTO;
import com.fr.service.ScoreBusinessService;
import com.fr.versionning.ApiVersion;
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
@ApiVersion("1")
public class ScoreAddController
{
	
	/** Service des scores. */
	private ScoreBusinessService scoreControllerService;
	
	/** Init score service. */
	@Autowired
	public void setScoreControllerService(final ScoreBusinessService scoreControllerService)
	{
		this.scoreControllerService = scoreControllerService;
	}
	
	/**
	 * Add score to a sppoti.
	 *
	 * @param scoreDTO
	 * 		score to add.
	 *
	 * @return added score.
	 */
	@PostMapping
	ResponseEntity<ScoreDTO> addScore(@RequestBody @Valid final ScoreDTO scoreDTO)
	{
		
		return new ResponseEntity<>(this.scoreControllerService.addScoreToSppoti(scoreDTO), HttpStatus.CREATED);
	}
}
