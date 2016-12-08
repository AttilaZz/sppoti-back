/**
 * 
 */
package com.fr.controllers;

import java.io.StringReader;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.fr.aop.TraceAuthentification;
import com.fr.controllers.service.PostControllerService;
import com.fr.models.ContentEditedResponse;
import com.fr.models.HeaderData;
import com.fr.models.JsonPostRequest;
import com.fr.models.PostRequest;
import com.fr.models.PostResponse;
import com.fr.entities.Address;
import com.fr.entities.EditHistory;
import com.fr.entities.LikeContent;
import com.fr.entities.Post;
import com.fr.entities.Sport;
import com.fr.entities.Sppoti;
import com.fr.entities.Users;

/**
 * Created by: Wail DJENANE on Jun 13, 2016
 */

@RestController
@RequestMapping("/post")
public class PostController {

	@Autowired
	private PostControllerService postDataService;

	private Logger LOGGER = Logger.getLogger(TraceAuthentification.class);

	private static final String ATT_USER_ID = "USER_ID";

	/*
	 * Add post to the timeline
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST, headers = "content-type=application/x-www-form-urlencoded")
	// @RequestMapping(value = "/add", method = RequestMethod.POST, consumes =
	// MediaType.APPLICATION_JSON_VALUE, produces =
	// MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PostResponse> addPost(@ModelAttribute JsonPostRequest json, HttpServletRequest request) {

		Gson gson = new Gson();

		PostRequest newPostReq = null;
		if (json != null) {
			try {

				LOGGER.info("POST-ADD: data sent by user: " + new ObjectMapper().writeValueAsString(json.getJson()));
				JsonReader reader = new JsonReader(new StringReader(json.getJson()));
				reader.setLenient(true);
				newPostReq = gson.fromJson(reader, PostRequest.class);
			} catch (JsonProcessingException e) {

				e.printStackTrace();
				LOGGER.info("POST-ADD: Data sent by user are invalid");
			}
		} else {
			LOGGER.info("POST-ADD: Data sent by user are invalid");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		// get current logged user
		Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
		Users user = postDataService.getUserById(userId);
		LOGGER.info("POST-ADD: LOGGED User: => " + userId);

		boolean canAdd = false;

		Sport targedSport = null;
		Sppoti game = null;
		Long sportId = null;
		Long gameId = null;

		Post newP = new Post(); // Object to save in database
		newP.setUser(user);

		PostResponse postRep = new PostResponse();// object to send on success

		// Sport is required
		if (newPostReq.getSportId() != null) {
			sportId = newPostReq.getSportId();
			targedSport = postDataService.getSportToUse(sportId);
			if (targedSport != null) {
				newP.setSport(targedSport);
				postRep.setSportId(sportId);
			} else {
				LOGGER.info("POST-ADD: The received sport ID is not valid");
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
		} else {
			LOGGER.info("POST-ADD: A sport must be defined ");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		}

		// if post is about a game
		if (newPostReq.getGameId() != null) {
			gameId = newPostReq.getGameId();
			game = postDataService.getGameById(gameId);
			if (game == null) {
				LOGGER.info("POST-ADD: Game id is not valid");
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			newP.setGame(game);
			postRep.setGame(game);
			canAdd = true;
		}

		// visibility
		int visibility = newPostReq.getVisibility();
		newP.setVisibility(visibility);
		postRep.setVisibility(visibility);

		String content = newPostReq.getContent().getContent();
		String[] image = newPostReq.getContent().getImageLink();
		String video = newPostReq.getContent().getVideoLink();
		// if post has only classic content - Text, Image, Video
		if (newPostReq.getContent() != null) {

			if (content == null && image == null && video == null) {
				LOGGER.info("POST-ADD: missing attributes");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			if (image != null && video != null) {
				LOGGER.info("POST-ADD: image OR video, make a choice !!");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			if (content != null) {
				if (content.trim().length() <= 0) {
					LOGGER.info("POST-ADD: Content value is empty");
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				}

				// base64 encode content
				// Charset.forName("UTF-8").encode(content);
				// Base64Utils.encode(content.getBytes())
				postRep.setContent(content);
				newP.setContent(content);

			}

			if (image != null) {
				if (image.length <= 0) {
					LOGGER.info("POST: imageLink value is empty");
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				}
				postRep.setImageLink(image);
				newP.setAlbum(image);
			}

			if (video != null) {
				if (video.trim().length() <= 0) {
					LOGGER.info("POST: videoLink value is empty");
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				}
				postRep.setVideoLink(video);
				newP.setVideoLink(video);
			}

			canAdd = true;

		}

		postRep.setFirstName(user.getFirstName());
		postRep.setLastName(user.getLastName());
		postRep.setUsername(user.getUsername());

		// if a game or one of the previous classic content has been assigned -
		// save post
		if (canAdd) {
			Long insertedPostId = (Long) postDataService.savePost(newP);
			Long id = newP.getId();
			if (id != null) {
				postRep.setId(id);
				postRep.setDatetimeCreated(newP.getDatetimeCreated());
				LOGGER.info("POST: post has been saved");

				/**
				 * prepare notifications
				 */
				// check if logged user has been tagged
				if (content != null) {
					postDataService.addNotification(userId, insertedPostId, content);
				}

				return new ResponseEntity<PostResponse>(postRep, HttpStatus.CREATED);
			} else {
				LOGGER.info("POST: Failed when saving the post in the DB");
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			// otherwise forbidden access
		} else {
			LOGGER.info("POST: At least a game or a post content must be assigned");
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}

	}

	/*
	 * Update post information
	 */
	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST, headers = "content-type=application/x-www-form-urlencoded")
	public ResponseEntity<ContentEditedResponse> updatePost(@PathVariable("id") Long postId,
			@ModelAttribute JsonPostRequest json) {

		Gson gson = new Gson();
		ContentEditedResponse newData = null;
		if (json != null) {
			try {
				newData = gson.fromJson(json.getJson(), ContentEditedResponse.class);

				LOGGER.info("POST_UPDATE: data sent by user: " + new ObjectMapper().writeValueAsString(newData));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		} else {
			LOGGER.info("POST_UPDATE: Data sent by user are invalid");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Post postToEdit = postDataService.findPost(postId);

		List<EditHistory> lastPostEditList = postDataService.getLastModification(postId);
		EditHistory lastPostEdit = null;
		boolean isAlreadyEdited = !lastPostEditList.isEmpty();

		EditHistory postEditRow = new EditHistory();
		Address postEditAddress = null;

		// Required attributes
		if (postToEdit == null) {
			LOGGER.info("POST_UPDATE: Failed to retreive the post");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		postEditRow.setPost(postToEdit);

		// if post has been edited before, get the latest entry
		if (isAlreadyEdited) {
			lastPostEdit = new EditHistory();
			lastPostEdit = lastPostEditList.get(0);
		}

		// test the received attributes content
		/*
		 * If post content has been edited -> New row in EDITHISTORY is created
		 * Otherwise: we just add a new row in address table related to the
		 * targeted post
		 */
		if (newData.getText() != null && newData.getText().trim().length() > 0) {
			postEditRow.setText(newData.getText());
			if (isAlreadyEdited) {
				postEditRow.setSport(lastPostEdit.getSport());
			} else {
				postEditRow.setSport(postToEdit.getSport());
			}

		} else if (newData.getLatitude() != 0.0 && newData.getLongitude() != 0.0) {
			// get old address -> update coordinate -> save as edited content
			// TODO: Location edit should be as same as text and sport
			postEditAddress = new Address();
			postEditAddress.setLatitude(newData.getLatitude());
			postEditAddress.setLongitude(newData.getLongitude());
			postEditAddress.setPost(postToEdit);

		} else if (newData.getSportId() != null) {
			// sport modification
			Sport sp = postDataService.getSportById(newData.getSportId());
			if (sp != null) {
				postEditRow.setSport(sp);
				if (isAlreadyEdited) {
					postEditRow.setText(lastPostEdit.getText());
				}
			} else {
				LOGGER.info("POST_UPDATE: Failed to retreive the sport to update");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} else {
			LOGGER.info("POST_UPDATE: No much found for the arguments sent");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		// if all arguments are correctly assigned - edit post
		if (postDataService.updatePost(postEditRow, postEditAddress))

		{
			LOGGER.info("POST_UPDATE: success");

			ContentEditedResponse edit = new ContentEditedResponse();
			edit.setId(postToEdit.getId());
			edit.setDateTime(postEditRow.getDatetimeEdited());
			edit.setText(postEditRow.getText());

			edit.setLatitude(newData.getLatitude());
			edit.setLongitude(newData.getLongitude());

			return new ResponseEntity<>(edit, HttpStatus.ACCEPTED);
		} else

		{
			LOGGER.info("POST_UPDATE: Failed when trying to update the post in DB");
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}

	}

	/*
	 * Delete the post
	 */
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public ResponseEntity<Void> deletePost(@PathVariable("id") Long id) {
		Post postToDelete = postDataService.findPost(id);

		if (postToDelete == null) {
			// post not fount
			LOGGER.info("POST_DELETE: Failed to retreive the post");
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		}

		if (postDataService.deletePost(postToDelete)) {
			// delete success
			LOGGER.info("POST_DELETE: Post with id:" + id + " has been deleted");
			return new ResponseEntity<>(HttpStatus.OK);
		}

		// database problem
		LOGGER.info("POST_DELETE: Database deleted problem !!");
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/*
	 * Like the post
	 */
	@RequestMapping(value = "/like/{id}", method = RequestMethod.GET)
	public ResponseEntity<Void> likePost(@PathVariable("id") Long id, HttpServletRequest request) {

		Post postToLike = postDataService.findPost(id);

		Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
		Users user = postDataService.getUserById(userId);

		if (postToLike == null || user == null) {

			if (postToLike == null) {
				// post not fount
				LOGGER.info("LIKE_POST: Failed to retreive the post");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				LOGGER.info("LIKE_POST: Failed to retreive user session");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

		}

		LikeContent likeToSave = new LikeContent();
		likeToSave.setPost(postToLike);
		likeToSave.setUser(user);

		if (!postDataService.isPostAlreadyLikedByUser(id, userId)) {
			if (postDataService.likePost(likeToSave)) {
				// delete success
				LOGGER.info("LIKE_POST: Post with id:" + id + " has been liked by: " + user.getFirstName() + " "
						+ user.getLastName());
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				LOGGER.info("LIKE_POST: Database like problem !!");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} else {
			LOGGER.info("LIKE_POST: Post already liked by: " + user.getFirstName() + " " + user.getLastName());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}

	/*
	 * Unlike the post
	 */
	@RequestMapping(value = "/unLike/{id}", method = RequestMethod.GET)
	public ResponseEntity<Void> unLikePost(@PathVariable("id") Long id, HttpServletRequest request) {

		Post commentToLike = postDataService.findPost(id);

		Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
		Users user = postDataService.getUserById(userId);

		if (commentToLike == null || user == null) {

			if (commentToLike == null) {
				// post not fount
				LOGGER.info("UNLIKE_POST: Failed to retreive the post");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				LOGGER.info("UNLIKE_POST: Failed to retreive user session");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

		}

		if (postDataService.isPostAlreadyLikedByUser(id, userId)) {

			if (postDataService.unLikePost(id, userId)) {
				// delete success
				LOGGER.info("UNLIKE_POST: Comment with id:" + id + " has been liked by: " + user.getFirstName() + " "
						+ user.getLastName());
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				LOGGER.info("UNLIKE_POST: Database like problem !!");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} else {
			LOGGER.info("UNLIKE_POST: Post NOT liked by: " + user.getFirstName() + " " + user.getLastName());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/*
	 * List of people who liked the post
	 */
	@RequestMapping(value = "/like/list/{id}/{page}", method = RequestMethod.GET)
	public ResponseEntity<PostResponse> getPostLikers(@PathVariable("id") Long id, @PathVariable("page") int page) {

		Post currentPost = postDataService.findPost(id);

		if (currentPost == null) {
			// post not fount
			LOGGER.info("LIKE_POST: Failed to retreive the post");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		List<HeaderData> likersList = postDataService.getLikersList(id, page);

		if (likersList.isEmpty()) {
			LOGGER.info("POST_LIKERS_LIST: Empty !!");
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		PostResponse pr = new PostResponse();
		pr.setPostLikers(likersList);
		pr.setLikeCount(currentPost.getLikes().size());

		return new ResponseEntity<>(pr, HttpStatus.OK);
	}

	@RequestMapping(value = "/gallery/photo/{page}", method = RequestMethod.GET)
	public ResponseEntity<List<PostResponse>> getGalleryPhoto(@PathVariable("page") int buttomMarker,
			HttpServletRequest request) {

		Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
		Users user = postDataService.getUserById(userId);

		List<PostResponse> lpost = postDataService.getPhotoGallery(userId, buttomMarker);

		if (lpost.size() == 0) {
			LOGGER.info("PHOTO_GALLERY: Empty !!");
			return new ResponseEntity<>(lpost, HttpStatus.NO_CONTENT);
		}

		LOGGER.info("PHOTO_GALLERY: has been returned to user:  " + user.getFirstName() + " " + user.getLastName());
		return new ResponseEntity<>(lpost, HttpStatus.OK);

	}

	@RequestMapping(value = "/gallery/video/{page}", method = RequestMethod.GET)
	public ResponseEntity<List<PostResponse>> getGalleryVideo(@PathVariable("page") int buttomMarker,
			HttpServletRequest request) {

		Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
		Users user = postDataService.getUserById(userId);

		List<PostResponse> lpost = postDataService.getVideoGallery(userId, buttomMarker);

		if (lpost.size() == 0) {
			LOGGER.info("VIDEO_GALLERY: Empty !!");
			return new ResponseEntity<>(lpost, HttpStatus.NO_CONTENT);
		}

		LOGGER.info("VIDEO_GALLERY: has been returned to user:  " + user.getFirstName() + " " + user.getLastName());
		return new ResponseEntity<>(lpost, HttpStatus.OK);

	}

	@RequestMapping(value = "/details/{id}", method = RequestMethod.GET)
	public ResponseEntity<PostResponse> detailsPost(@PathVariable("id") Long id, HttpServletRequest request) {

		Post mPost = postDataService.findPost(id);

		Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
		Users user = postDataService.getUserById(userId);

		if (mPost == null || user == null) {

			if (mPost == null) {
				// post not fount
				LOGGER.info("DETAILS_POST: Failed to retreive the post");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				LOGGER.info("DETAILS_POST: Failed to retreive user session");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

		}

		PostResponse prep = postDataService.fillPostToSend(userId);
		LOGGER.info("DETAILS_POST: Post details has been returned for postId:" + id);
		return new ResponseEntity<>(prep, HttpStatus.OK);

	}

	@RequestMapping(value = "/edithistory/{id}/{page}", method = RequestMethod.GET)
	public ResponseEntity<List<ContentEditedResponse>> editHistory(@PathVariable("id") Long id,
			@PathVariable("page") int page, HttpServletRequest request) {

		Post postToLike = postDataService.findPost(id);

		if (postToLike == null) {

			// post not fount
			LOGGER.info("POST_EDIT_HISTORY: Failed to retreive the post");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		}

		LOGGER.info("POST_EDIT_HISTORY: Post HISTORY has been returned for postId:" + id);
		return new ResponseEntity<>(postDataService.getAllPostHistory(id, page), HttpStatus.OK);

	}

	@RequestMapping(value = "/edithistory/{id}/{visibility}", method = RequestMethod.GET)
	public ResponseEntity<Void> editVisibility(@PathVariable("id") Long id, @PathVariable("visibility") int visibility,
			HttpServletRequest request) {

		Post postToEdit = postDataService.findPost(id);

		if (postToEdit == null) {

			// post not fount
			LOGGER.info("POST_EDIT_VISIBILITY: Failed to retreive the post");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		}

		if (!postDataService.editPostVisibility(id, visibility)) {
			LOGGER.info("POST_EDIT_VISIBILITY: Can't update visibility!!");
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		}

		LOGGER.info("POST_EDIT_VISIBILITY: Post VISIBILITY has been changed for postId:" + id);
		return new ResponseEntity<>(HttpStatus.OK);

	}
}
