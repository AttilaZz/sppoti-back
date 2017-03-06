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
 * Created by wdjenane on 22/02/2017.
 */
@RestController
@RequestMapping("/gallery/video/{userId}")
public class GalleryVideoController {
    private PostControllerService postControllerService;

    @Autowired
    public void setPostControllerService(PostControllerService postControllerService) {
        this.postControllerService = postControllerService;
    }

    private Logger LOGGER = Logger.getLogger(GalleryVideoController.class);


    /**
     * @param page page number.
     * @return all videos posted by user.
     */
    @GetMapping(value = "/{page}")
    public ResponseEntity<List<PostResponseDTO>> videoGallery(@PathVariable int page, @PathVariable int userId) {

        List<PostResponseDTO> videoGallery = postControllerService.getVideoGallery(userId, page);

        LOGGER.info("Video gallery has been returned: " + videoGallery);
        return new ResponseEntity<>(videoGallery, HttpStatus.OK);

    }
}
