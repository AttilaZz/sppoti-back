package com.fr.rest.controllers.sppoti;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fr.commons.dto.SppotiResponse;
import com.fr.rest.service.SppotiControllerService;
import com.fr.commons.dto.SppotiRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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
     * @param response
     * @param request
     * @return 201 status && Sppoti object with the inserted data, 400 status otherwise.
     * @throws IOException
     */
    @PostMapping
    public ResponseEntity addPost(@RequestBody SppotiRequest newSppoti, HttpServletResponse response, HttpServletRequest request) throws IOException {

        if (newSppoti.getAddress() == null || newSppoti.getAddress().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Address not found");
        }
//        if (newSppoti.getDescription() == null || newSppoti.getDescription().isEmpty()) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Description not found");
//        }
        if (newSppoti.getMaxTeamCount() == 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Max-TeamRequest-Count not found");
        }
        if (newSppoti.getSportId() == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Sport-Id not found");
        }
//        if (newSppoti.getTags() == null || newSppoti.getTags().isEmpty()) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tags not found");
//        }
        if (newSppoti.getTitre() == null || newSppoti.getTitre().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Title not found");
        }
        if (newSppoti.getDatetimeStart() == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Date-start not found");
        }
        if (newSppoti.getMyTeamId() == 0 && newSppoti.getMyTeam() == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "TeamHostModel && TeamHostId not found");
        }
//        if (newSppoti.getVsTeam() == 0) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Adverse team id not found");
//        }
        Long sppotiCreator = (Long) request.getSession().getAttribute(ATT_USER_ID);

        try {
            SppotiResponse sppotiResponse = sppotiControllerService.saveSppoti(newSppoti, sppotiCreator);

            ObjectMapper mapper = new ObjectMapper();
            String jsonInString = mapper.writeValueAsString(sppotiResponse);

            return ResponseEntity.status(HttpStatus.CREATED).body(jsonInString);
        } catch (RuntimeException e) {
            e.printStackTrace();
            LOGGER.error("Ajout de sppoti imposssible: " + e.getMessage());
            return ResponseEntity.badRequest().body("Ajout de sppoti imposssible");
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
            LOGGER.error("Impossible de supprimer le sppoti: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

}