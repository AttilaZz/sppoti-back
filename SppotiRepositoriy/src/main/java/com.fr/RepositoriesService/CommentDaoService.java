package com.fr.RepositoriesService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fr.pojos.Comment;

/**
 * Created by: Wail DJENANE on Aug 12, 2016
 */
@Service
public interface CommentDaoService extends GenericDaoService<Comment, Integer> {

	/**
	 * @param postId
	 * @param buttomMarker
	 * @return
	 */
	List<Comment> getCommentsFromLastMajId(Long postId, int buttomMarker);

	/**
	 * @param postId
	 * @return
	 */
	List<Comment> getLastPostComment(Long postId);

	/**
	 * @param postId
	 * @return
	 */
	Long getCommentCount(Long postId);


}
