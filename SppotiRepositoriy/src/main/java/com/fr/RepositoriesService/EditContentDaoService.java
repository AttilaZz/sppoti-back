package com.fr.RepositoriesService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fr.entities.EditHistory;

/**
 * Created by: Wail DJENANE on Aug 20, 2016
 */
@Service
public interface EditContentDaoService extends GenericDaoService<EditHistory, Integer> {

	public List<EditHistory> getLastEditedPost(Long postId);

	/**
	 * @param id
	 * @param page
	 * @return 
	 */
	public List<EditHistory> getAllPostHistory(Long id, int page);

	/**
	 * @param id
	 * @param page
	 * @return
	 */
	public List<EditHistory> getAllComenttHistory(Long id, int page);

	/**
	 * @param commentId
	 * @return
	 */
	List<EditHistory> getLastEditedComent(Long commentId);
}
