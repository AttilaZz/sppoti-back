package com.fr.api.find;

import com.fr.commons.dto.UserDTO;
import com.fr.service.SppotiControllerService;
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
 * Created by djenanewail on 5/10/17.
 */

@RestController
@RequestMapping("/find/sppoter")
@ApiVersion("1")
public class FindSppoterController
{
	/** Sppoti controller service. */
	private final SppotiControllerService sppotiControllerService;
	
	/** Init service. */
	@Autowired
	public FindSppoterController(final SppotiControllerService sppotiControllerService)
	{
		this.sppotiControllerService = sppotiControllerService;
	}
	
	/**
	 * @param prefix
	 * 		sppoter name prefix.
	 * @param sppotiId
	 * 		sppoti id.
	 * @param page
	 * 		page number.
	 *
	 * @return list of sppoter.
	 */
	@GetMapping("/{sppotiId}/{prefix}/{page}")
	ResponseEntity<List<UserDTO>> findMyTeams(@PathVariable final String prefix, @PathVariable final String sppotiId,
											  @PathVariable final int page)
	{
		
		return new ResponseEntity<>(
				this.sppotiControllerService.findAllSppoterAllowedToJoinSppoti(prefix, sppotiId, page), HttpStatus.OK);
	}
}
