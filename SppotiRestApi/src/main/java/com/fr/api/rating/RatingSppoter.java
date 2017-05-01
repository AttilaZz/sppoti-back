package com.fr.api.rating;

import com.fr.commons.dto.SppotiRatingDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.service.SppotiControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * Created by wdjenane on 14/03/2017.
 */
@RestController
@RequestMapping("/rating")
class RatingSppoter
{
	
	/**
	 * Sppoti controller service.
	 */
	private SppotiControllerService sppotiControllerService;
	
	/**
	 * Init services.
	 */
	@Autowired
	void setSppotiControllerService(SppotiControllerService sppotiControllerService)
	{
		this.sppotiControllerService = sppotiControllerService;
	}
	
	/**
	 * Evaluate other sppoters in same sppoti.
	 */
	@PostMapping("/sppoter/{sppotiId}")
	ResponseEntity<List<UserDTO>> rateSppoter(@PathVariable int sppotiId,
											  @RequestBody List<SppotiRatingDTO> sppotiRatingDTO)
	{
		
		sppotiRatingDTO.forEach(sp -> {
			if (sp.getStars() == null || (sp.getStars() > 10 || sp.getStars() < 0)) {
				throw new BusinessGlobalException("Stars count are not correct in the request");
			}
			
			if (sp.getSppoterRatedId() == null) {
				throw new BusinessGlobalException("Rated sppoter id is required");
			}
		});
		
		return new ResponseEntity<>(sppotiControllerService.rateSppoters(sppotiRatingDTO, sppotiId),
				HttpStatus.CREATED);
	}
	
}
