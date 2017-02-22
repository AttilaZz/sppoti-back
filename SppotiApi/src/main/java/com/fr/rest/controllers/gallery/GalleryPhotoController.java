package com.fr.rest.controllers.post;

import com.fr.commons.dto.PostResponseDTO;
import com.fr.commons.dto.UserDTO;
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

    @Autowired
    public void setPostControllerService(PostControllerService postControllerService) {
        this.postControllerService = postControllerService;
    }

    private static final String ATT_USER_ID = "USER_ID";

    /**
     * @param page page number.
     * @param request http request object.
     * @return List of all photos posted by a user.
     */
    @GetMapping(value = "/photo/{page}")
    public ResponseEntity<List<PostResponseDTO>> photoGallery(@PathVariable int page, HttpServletRequest request) {

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);

        List<PostResponseDTO> lpost = postControllerService.getPhotoGallery(userId, page);

        return new ResponseEntity<>(lpost, HttpStatus.OK);

    }

    /**
     *
     * @param page page number.
     * @param request http request object.
     * @return all videos posted by user.
     */
    @GetMapping(value = "/video/{page}")
    public ResponseEntity<List<PostResponseDTO>> videoGallery(@PathVariable int page, HttpServletRequest request) {

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);

        List<PostResponseDTO> lpost = postControllerService.getVideoGallery(userId, page);

        return new ResponseEntity<>(lpost, HttpStatus.OK);

    }

}