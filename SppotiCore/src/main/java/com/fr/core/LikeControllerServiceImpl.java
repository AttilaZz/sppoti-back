package com.fr.core;

import com.fr.entities.CommentEntity;
import com.fr.rest.service.LikeControllerService;
import com.fr.entities.LikeContent;
import com.fr.entities.Post;
import com.fr.commons.dto.HeaderDataDTO;
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


    @Value("${key.likesPerPage}")
    private int likeSize;

    @Override
    public boolean likePost(LikeContent likeToSave) {
        return likeContent(likeToSave);
    }

    @Override
    public boolean unLikePost(Post post) {
        try {
            LikeContent likeContent = likeRepository.getByPostId(post.getId());
            likeRepository.delete(likeContent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean isPostAlreadyLikedByUser(int postId, Long userId) {

        return likeRepository.getByUserIdAndPostUuid(userId, postId) != null;

    }

    @Override
    public List<HeaderDataDTO> getPostLikersList(int id, int page) {


        Pageable pageable1 = new PageRequest(page, likeSize);

        List<LikeContent> likersData = likeRepository.getByPostUuidOrderByDatetimeCreated(id, pageable1);

        return likersList(likersData);

    }

    @Override
    public List<HeaderDataDTO> getCommentLikersList(int id, int page) {

        Pageable pageable1 = new PageRequest(page, likeSize);

        List<LikeContent> likersData = likeRepository.getByCommentUuidOrderByDatetimeCreated(id, pageable1);

        return likersList(likersData);

    }

    @Override
    public boolean unLikeComment(CommentEntity commentEntityToUnlike) {
        try {
            LikeContent likeContent = likeRepository.getByCommentId(commentEntityToUnlike.getId());
            likeRepository.delete(likeContent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isCommentAlreadyLikedByUser(int commentId, Long userId) {
        return likeRepository.getByUserIdAndCommentUuid(userId, commentId) != null;

    }

    @Override
    public boolean likeComment(LikeContent likeToSave) {
        return likeContent(likeToSave);
    }

    //like content - Post or CommentEntity
    private boolean likeContent(LikeContent likeContent) {
        try {
            likeRepository.save(likeContent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private List likersList(List<LikeContent> likersData) {

        List<HeaderDataDTO> likers = new ArrayList<HeaderDataDTO>();

        if (!likersData.isEmpty()) {
            for (LikeContent row : likersData) {
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
