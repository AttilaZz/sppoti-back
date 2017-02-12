package com.fr.rest.controllers.post;

import com.fr.commons.dto.PostResponseDTO;
import com.fr.entities.UserEntity;
import com.fr.rest.service.CommentControllerService;
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
@RequestMapping("/gallery")
public class GalleryController {

    private PostControllerService postControllerService;
    private CommentControllerService commentControllerService;

    @Autowired
    public void setCommentControllerService(CommentControllerService commentControllerService) {
        this.commentControllerService = commentControllerService;
    }

    @Autowired
    public void setPostControllerService(PostControllerService postControllerService) {
        this.postControllerService = postControllerService;
    }

    private Logger LOGGER = Logger.getLogger(GalleryController.class);

    private static final String ATT_USER_ID = "USER_ID";

    /**
     * @param page
     * @param request
     * @return List of all photos posted by a usert
     */
    @GetMapping(value = "/photo/{page}")
    public ResponseEntity<List<PostResponseDTO>> photoGallery(@PathVariable int page, HttpServletRequest request) {

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        UserEntity user = postControllerService.getUserById(userId);

        List<PostResponseDTO> lpost = postControllerService.getPhotoGallery(userId, page);

        if (lpost.size() == 0) {
            LOGGER.info("PHOTO_GALLERY: Empty !!");
            return new ResponseEntity<>(lpost, HttpStatus.NO_CONTENT);
        }

        LOGGER.info("PHOTO_GALLERY: has been returned to user:  " + user.getFirstName() + " " + user.getLastName());
        return new ResponseEntity<>(lpost, HttpStatus.OK);

    }

    @GetMapping(value = "/video/{page}")
    public ResponseEntity<List<PostResponseDTO>> videoGallery(@PathVariable int page, HttpServletRequest request) {

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        UserEntity user = postControllerService.getUserById(userId);

        List<PostResponseDTO> lpost = postControllerService.getVideoGallery(userId, page);

        if (lpost.size() == 0) {
            LOGGER.info("VIDEO_GALLERY: Empty !!");
            return new ResponseEntity<>(lpost, HttpStatus.NO_CONTENT);
        }

        LOGGER.info("VIDEO_GALLERY: has been returned to user:  " + user.getFirstName() + " " + user.getLastName());
        return new ResponseEntity<>(lpost, HttpStatus.OK);

    }

}