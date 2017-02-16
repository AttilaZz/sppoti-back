package com.fr.core;

import com.fr.commons.dto.HeaderDataDTO;
import com.fr.entities.CommentEntity;
import com.fr.entities.LikeContentEntity;
import com.fr.entities.PostEntity;
import com.fr.models.NotificationType;
import com.fr.rest.service.LikeControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by djenanewail on 12/24/16.
 */
@Component
public class LikeControllerServiceImpl extends AbstractControllerServiceImpl implements LikeControllerService {

    private Logger LOGGER = Logger.getLogger(LikeControllerServiceImpl.class);

    @Value("${key.likesPerPage}")
    private int likeSize;

    @Override
    public LikeContentEntity likePost(LikeContentEntity likeToSave) {
        return likeContent(likeToSave);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unLikePost(PostEntity post) {

        LikeContentEntity likeContent = likeRepository.getByPostId(post.getId());
        likeRepository.delete(likeContent);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPostAlreadyLikedByUser(int postId, Long userId) {

        return likeRepository.getByUserIdAndPostUuid(userId, postId) != null;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<HeaderDataDTO> getPostLikersList(int id, int page) {


        Pageable pageable1 = new PageRequest(page, likeSize);

        List<LikeContentEntity> likersData = likeRepository.getByPostUuidOrderByDatetimeCreated(id, pageable1);

        return likersList(likersData);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<HeaderDataDTO> getCommentLikersList(int id, int page) {

        Pageable pageable1 = new PageRequest(page, likeSize);

        List<LikeContentEntity> likersData = likeRepository.getByCommentUuidOrderByDatetimeCreated(id, pageable1);

        return likersList(likersData);

    }

    /**
     * {@inheritDoc}
     */

    @Override
    public void unLikeComment(CommentEntity commentEntityToUnlike) {

        LikeContentEntity likeContent = likeRepository.getByCommentId(commentEntityToUnlike.getId());
        likeRepository.delete(likeContent);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCommentAlreadyLikedByUser(int commentId, Long userId) {
        return likeRepository.getByUserIdAndCommentUuid(userId, commentId) != null;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void likeComment(LikeContentEntity likeToSave) {

        likeContent(likeToSave);

    }

    /**
     * like content - PostEntity or CommentEntity
     *
     * @param likeContent
     * @return boolean
     */
    private LikeContentEntity likeContent(LikeContentEntity likeContent) {

        if (likeContent != null) {

            if (likeContent.getComment() != null && !likeContent.getComment().getUser().getId().equals(getConnectedUser().getId())) {
                //Comment like
                addNotification(NotificationType.X_LIKED_YOUR_COMMENT, likeContent.getUser(), likeContent.getComment().getUser());

            } else if (likeContent.getPost() != null && !likeContent.getPost().getUser().getId().equals(getConnectedUser().getId())) {
                //like post
                addNotification(NotificationType.X_LIKED_YOUR_POST, likeContent.getUser(), likeContent.getPost().getUser());
            }

        }

        return likeRepository.save(likeContent);

    }

    /**
     * get likers list
     *
     * @param likersData
     * @return list of HeaderDataDTO
     */
    private List<HeaderDataDTO> likersList(List<LikeContentEntity> likersData) {

        List<HeaderDataDTO> likers = new ArrayList<HeaderDataDTO>();

        if (!likersData.isEmpty()) {
            for (LikeContentEntity row : likersData) {
                // get liker data
                HeaderDataDTO u = new HeaderDataDTO();
//                u.setAvatar(userDaoService.getLastAvatar(row.getUser().getId()).get(0).getUrl());
                u.setFirstName(row.getUser().getFirstName());
                u.setLastName(row.getUser().getLastName());
                // u.setCover(userDao.getLastCover(row.getUser().getId(),
                // coverType));
                u.setUsername(row.getUser().getUsername());

                likers.add(u);
            }
        }

        return likers;
    }
}
