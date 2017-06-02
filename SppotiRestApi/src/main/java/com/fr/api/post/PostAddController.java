package com.fr.api.post;

import com.fr.commons.dto.post.PostDTO;
import com.fr.commons.dto.post.PostRequestDTO;
import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.commons.exception.PostContentMissingException;
import com.fr.entities.*;
import com.fr.service.PostControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by djenanewail on 2/13/17.
 */

@RestController
@RequestMapping("/post")
class PostAddController
{
	
	/** Post controller service. */
	private PostControllerService postDataService;
	
	/** Init post service. */
	@Autowired
	void setPostDataService(final PostControllerService postDataService)
	{
		this.postDataService = postDataService;
	}
	
	/**
	 * @param newPostReq
	 * 		post data to save.
	 *
	 * @return Add post by user.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping
	ResponseEntity<PostDTO> addPost(@RequestBody final PostRequestDTO newPostReq, final Authentication authentication)
	{
		
		// get current logged user
		final Long userId = ((AccountUserDetails) authentication.getPrincipal()).getId();
		final UserEntity user = this.postDataService.getUserById(userId);
		
		boolean canAdd = false;
		
		final SportEntity targedSport;
		final SppotiEntity game;
		final Long sportId;
		final Long gameId;
		
		final PostEntity newPostToSave = new PostEntity(); // Object to save in database
		newPostToSave.setUser(user);
		
		final PostDTO postRep = new PostDTO();// object to send on success
		
		// SportEntity is required
		if (newPostReq.getSportId() != null) {
			
			sportId = newPostReq.getSportId();
			targedSport = this.postDataService.getSportToUse(sportId);
			
			if (targedSport != null) {
				newPostToSave.setSport(targedSport);
				postRep.setSportId(sportId);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		// if post is about a game
		if (newPostReq.getGameId() != null) {
			
			gameId = newPostReq.getGameId();
			game = this.postDataService.getSppotiById(gameId);
			
			if (game == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
			//            newPostToSave.setSppoti(game);
			//            postRep.setGame(game);
			canAdd = true;
			
		}
		
		// visibility
		final int visibility = newPostReq.getVisibility();
		newPostToSave.setVisibility(visibility);
		postRep.setVisibility(visibility);

        /*
			---- Manage address
         */
		
		//AddressEntity address = newPostReq.getAddress();
		
		//        if (address != null) {
		//            address.setPost(newPostToSave);
		//            SortedSet<AddressEntity> addresses = new TreeSet<>();
		//            addresses.add(address);
		//
		//            newPostToSave.setAddresses(addresses);
		//        }

        /*
			---- End address
         */
		final String content;
		final Set<String> image;
		final String video;
		
		content = newPostReq.getContent().getContent();
		image = newPostReq.getContent().getImageLink();
		video = newPostReq.getContent().getVideoLink();
		
		// if post has only classic content - Text, Image, Video
		if (newPostReq.getContent() != null) {
			
			if (content == null && image == null && video == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
			if (image != null && video != null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
			if (content != null) {
				if (content.trim().length() <= 0) {
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
				
				postRep.setContent(content);
				newPostToSave.setContent(content);
			}
			
			if (image != null) {
				if (image.size() <= 0) {
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
				postRep.setImageLink(image);
				newPostToSave.setAlbum(image);
			}
			
			if (video != null) {
				if (video.trim().length() <= 0) {
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
				postRep.setVideoLink(video);
				newPostToSave.setVideo(video);
			}
			
			canAdd = true;
			
		}
		
		postRep.setFirstName(user.getFirstName());
		postRep.setLastName(user.getLastName());
		postRep.setUsername(user.getUsername());
		
		final List<ResourcesEntity> resources = new ArrayList<>();
		resources.addAll(user.getResources());
		
		if (!resources.isEmpty()) {
			if (resources.get(0) != null && resources.get(0).getType() == 1) {
				postRep.setAvatar(resources.get(0).getUrl());
			} else if (resources.get(1) != null && resources.get(1).getType() == 1) {
				postRep.setAvatar(resources.get(1).getUrl());
			}
		}

        /*
		Check target user
         */
		final int requestTargetUserId = newPostReq.getTargetUserUuid();
		final UserEntity targetUser = this.postDataService.getUserByUuId(requestTargetUserId);
		if (requestTargetUserId != 0 && targetUser != null) {
			
			newPostToSave.setTargetUserProfileUuid(newPostReq.getTargetUserUuid());
			postRep.setTargetUser(targetUser.getFirstName(), targetUser.getLastName(), targetUser.getUsername(),
					targetUser.getUuid(), false);
		} else if (requestTargetUserId != 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		// if a game or one of the previous classic content has been assigned -
		// save post
		
		try {
			if (!canAdd)
				throw new PostContentMissingException("At least a game or a post content must be assigned");
			//Save and get the inserted id
			
			final int insertedPostId = this.postDataService.savePost(newPostToSave).getUuid();
			
			//Fill the id in the response object
			postRep.setId(insertedPostId);
			postRep.setDatetimeCreated(newPostToSave.getDatetimeCreated());
			
			postRep.setMyPost(true);
			return new ResponseEntity<>(postRep, HttpStatus.CREATED);
			
		} catch (final PostContentMissingException e1) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		}
		
	}
	
}
