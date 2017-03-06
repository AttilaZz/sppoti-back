package com.fr.rest.controllers.gallery;

import com.fr.commons.dto.post.PostResponseDTO;
import com.fr.rest.service.PostControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by djenanewail on 12/24/16.
 */

@RestController
@RequestMapping("/gallery/photo/{userId}")
public class GalleryPhotoController {

    private PostControllerService postControllerService;

    @Autowired
    public void setPostControllerService(PostControllerService postControllerService) {
        this.postControllerService = postControllerService;
    }

    private Logger LOGGER = Logger.getLogger(GalleryPhotoController.class);

    /**
     * @param page    page number.
     * @return List of all photos posted by a user.
     */
    @GetMapping(value = "/{page}")
    public ResponseEntity<List<PostResponseDTO>> photoGallery(@PathVariable int page, @PathVariable int userId) {

        List<PostResponseDTO> photoGallery = postControllerService.getPhotoGallery(userId, page);

        LOGGER.info("Photos gallery has been returned: " + photoGallery);
        return new ResponseEntity<>(photoGallery, HttpStatus.OK);

    }

}