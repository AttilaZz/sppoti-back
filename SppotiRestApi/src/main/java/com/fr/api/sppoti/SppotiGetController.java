package com.fr.api.sppoti;

import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.service.SppotiBusinessService;
import com.fr.versionning.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by djenanewail on 2/5/17.
 */
@RestController
@RequestMapping("/sppoti")
@ApiVersion("1")
class SppotiGetController
{
	
	/** Sppoti controller service. */
	private SppotiBusinessService sppotiControllerService;
	
	/** Init Service. */
	@Autowired
	void setSppotiControllerService(final SppotiBusinessService sppotiControllerService)
	{
		this.sppotiControllerService = sppotiControllerService;
	}
	
	/**
	 * @param id
	 * 		sppoti id to find.
	 *
	 * @return 200 status with the target sppoti, 400 status otherwise.
	 */
	@GetMapping("/{id}")
	ResponseEntity<SppotiDTO> getSppotiById(@PathVariable final String id)
	{
		
		return new ResponseEntity<>(this.sppotiControllerService.getSppotiByUuid(id), HttpStatus.OK);
		
	}
	
	/**
	 * Get All sppoties created by the given user id.
	 *
	 * @param id
	 * 		user id.
	 * @param page
	 * 		page number.
	 *
	 * @return List of {@link SppotiDTO}
	 */
	@GetMapping("/all/{userId}/{page}")
	ResponseEntity<List<SppotiDTO>> getAllUserSppoties(@PathVariable("userId") final String id,
													   @PathVariable final int page)
	{
		
		return new ResponseEntity<>(this.sppotiControllerService.getAllUserSppoties(id, page), HttpStatus.OK);
		
	}
	
	/**
	 * Get All sppoties that user joined.
	 *
	 * @param id
	 * 		user id.
	 * @param page
	 * 		page number.
	 *
	 * @return List of {@link SppotiDTO}
	 */
	@GetMapping("/all/joined/{userId}/{page}")
	ResponseEntity<List<SppotiDTO>> getAllJoinedUserSppoties(@PathVariable("userId") final String id,
															 @PathVariable    final int page)
	{
		
		return new ResponseEntity<>(this.sppotiControllerService.getAllJoinedSppoties(id, page), HttpStatus.OK);
		
	}
	
	/**
	 * All confirmed sppoties that user joined.
	 *
	 * @param userId
	 * 		user id.
	 * @param page
	 * 		page number.
	 *
	 * @return List of {@link SppotiDTO}
	 */
	@GetMapping("/all/confirmed/{userId}/{page}")
	ResponseEntity<List<SppotiDTO>> getAllConfirmedSppoties(@PathVariable final String userId,
															@PathVariable final int page)
	{
		return new ResponseEntity<>(this.sppotiControllerService.getAllConfirmedSppoties(userId, page), HttpStatus.OK);
	}
	
	/**
	 * Get All refused sppoties that user asked to join.
	 *
	 * @param userId
	 * 		user id.
	 * @param page
	 * 		page number.
	 *
	 * @return List of {@link SppotiDTO}
	 */
	@GetMapping("/all/refused/{userId}/{page}")
	ResponseEntity<List<SppotiDTO>> getAllRefusedSppoties(@PathVariable final String userId, @PathVariable final int page)
	{
		
		return new ResponseEntity<>(this.sppotiControllerService.getAllRefusedSppoties(userId, page), HttpStatus.OK);
		
	}
	
	
	/**
	 * Get all upcoming sppoties.
	 *
	 * Any sppoti where:
	 *
	 * - I am an admin.
	 * - I am a member in the team host.
	 * - I am a confirmed member in the adverse team.
	 *
	 * @param userId
	 * 		user id.
	 * @param page
	 * 		page number.
	 *
	 * @return List of {@link SppotiDTO}
	 */
	@GetMapping("/all/upcoming/{userId}/{page}")
	ResponseEntity<List<SppotiDTO>> getAllUpcomingSppoties(@PathVariable final String userId, @PathVariable final int page)
	{
		
		return new ResponseEntity<>(this.sppotiControllerService.getAllUpcomingSppoties(userId, page), HttpStatus.OK);
		
	}
	
	/**
	 * Get all sppoties where challenge has been sent and waiting for response.
	 *
	 * @param userId
	 * 		user id.
	 * @param page
	 * 		page number.
	 *
	 * @return list of sppoties.
	 */
	@GetMapping("/all/pending/challenge/request/{userId}/{page}")
	ResponseEntity<List<SppotiDTO>> getAllPendingChallengeRequest(@PathVariable final String userId,
																  @PathVariable final int page)
	{
		
		return new ResponseEntity<>(this.sppotiControllerService.getAllPendingChallengeRequestSppoties(userId, page),
				HttpStatus.OK);
		
	}
	
	
}
