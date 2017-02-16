package com.fr.rest.controllers.sppoti;

import com.fr.commons.dto.SppotiRequestDTO;
import com.fr.commons.dto.SppotiResponseDTO;
import com.fr.rest.service.SppotiControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by: Wail DJENANE on Jul 11, 2016
 */

@RestController
@RequestMapping("/sppoti")
public class SppotiAddController {

    private SppotiControllerService sppotiControllerService;

    @Autowired
    public void setSppotiControllerService(SppotiControllerService sppotiControllerService) {
        this.sppotiControllerService = sppotiControllerService;
    }

    private Logger LOGGER = Logger.getLogger(SppotiAddController.class);

    private static final String ATT_USER_ID = "USER_ID";

    /**
     * @param newSppoti
     * @param request
     * @return 201 status && SppotiEntity object with the inserted data, 400 status otherwise.
     */
    @PostMapping
    public ResponseEntity<SppotiResponseDTO> addPost(@RequestBody SppotiRequestDTO newSppoti, HttpServletRequest request) {

        if (newSppoti.getAddress() == null || newSppoti.getAddress().isEmpty()) {
            LOGGER.error("AddressEntity not found");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
//        if (newSppoti.getDescription() == null || newSppoti.getDescription().isEmpty()) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Description not found");
//        }
        if (newSppoti.getMaxTeamCount() == 0) {
            LOGGER.error("Max-TeamRequestDTO-Count not found");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
        if (newSppoti.getSportId() == null) {
            LOGGER.error("SportEntity-Id not found");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
//        if (newSppoti.getTags() == null || newSppoti.getTags().isEmpty()) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tags not found");
//        }
        if (newSppoti.getTitre() == null || newSppoti.getTitre().isEmpty()) {
            LOGGER.error("Title not found");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (newSppoti.getDatetimeStart() == null) {
            LOGGER.error("Date-start not found");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (newSppoti.getMyTeamId() == 0 && newSppoti.getMyTeam() == null) {
            LOGGER.error("TeamHostModel && TeamHostId not found ");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
//        if (newSppoti.getVsTeam() == 0) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Adverse team id not found");
//        }

        Long sppotiCreator = (Long) request.getSession().getAttribute(ATT_USER_ID);

        try {
            SppotiResponseDTO sppotiResponseDTO = sppotiControllerService.saveSppoti(newSppoti, sppotiCreator);

            return new ResponseEntity<>(sppotiResponseDTO, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            LOGGER.error("Ajout de sppoti imposssible: ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * @param id
     * @return 200 status if deleted, 400 status otherwise.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteSppoti(@PathVariable int id) {

        try {
            sppotiControllerService.deleteSppoti(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (RuntimeException e) {
            LOGGER.error("Impossible de supprimer le sppoti: ", e);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

}