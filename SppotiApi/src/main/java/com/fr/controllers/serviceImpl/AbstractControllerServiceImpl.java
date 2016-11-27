package com.fr.controllers.serviceImpl;

import com.fr.RepositoriesService.*;
import com.fr.controllers.SignUpController;
import com.fr.controllers.service.AbstractControllerService;
import com.fr.mail.ApplicationMailer;
import com.fr.models.CommentModel;
import com.fr.models.ContentEditedResponse;
import com.fr.pojos.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component("abstractService")
@PropertySource(value = { "classpath:email.properties" })
public abstract class AbstractControllerServiceImpl implements AbstractControllerService {

	protected Long connectedUserId;

	protected UserRepository userRepository;

	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Autowired
	protected UserDaoService userDaoService;

	@Autowired
	protected NotificationDaoService notificationDaoService;

	@Autowired
	protected PostDaoService postDaoService;

	@Autowired
	protected ProfileDaoService profileDaoService;

	@Autowired
	protected SportDaoService sportDaoService;

	@Autowired
	protected MessageDaoService messageDaoService;

	@Autowired
	protected AddressDaoService addressDaoService;

	@Autowired
	protected GameDaoService sppotiDaoService;

	@Autowired
	protected FriendShipDaoService friendDaoService;

	@Autowired
	protected ApplicationMailer mailer;

	@Autowired
	private Environment environment;

	@Autowired
	protected LikeDaoService likeDaoService;

	@Autowired
	protected CommentDaoService commentDaoService;

	@Autowired
	protected EditContentDaoService editContentDaoService;

	@Autowired
	protected ResourceDaoService resourceDaoService;

	protected Logger LOGGER = Logger.getLogger(SignUpController.class);

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getUserRole() {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ArrayList<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();

		List<String> userRoles = new ArrayList<>();

		if (principal instanceof UserDetails) {
			roles = (ArrayList<GrantedAuthority>) ((UserDetails) principal).getAuthorities();
			for (GrantedAuthority role : roles) {
				userRoles.add(role.getAuthority());
			}
		}
		return userRoles;
	}

	@Override
	public String getAuthenticationUsername() {
		String userName = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails) principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}

	@Override
	public Users getUserFromUsernameType(String loginUser) {
		return null;
	}

	@Override
	public int getUserLoginType(String username) {
		String numberRegex = "[0-9]+";
		int loginType;

		if (username.contains("@")) {
			loginType = 2;
		} else if (username.matches(numberRegex)) {
			loginType = 3;
		} else {
			loginType = 1;
		}

		return loginType;
	}

	@Override
	public Users getUserById(Long id) {
		return userDaoService.getEntityByID(id);
	}

	protected Properties globalAddressConfigProperties() {
		Properties properties = new Properties();
		properties.put("rootAddress", environment.getRequiredProperty("rootAddress"));

		return properties;
	}

	protected List<ContentEditedResponse> fillEditContentResponse(List<EditHistory> dsHistoryList) {
		List<ContentEditedResponse> editHistoryResponse = new ArrayList<>();
		editHistoryResponse.clear();
		for (EditHistory editContent : dsHistoryList) {

			ContentEditedResponse cer = new ContentEditedResponse();
			cer.setDateTime(editContent.getDatetimeEdited());
			cer.setId(editContent.getId());
			cer.setText(editContent.getText());

			editHistoryResponse.add(cer);
		}

		return editHistoryResponse;
	}

	protected List<CommentModel> fillCommentModelList(List<Comment> dbCommentList, Long userId) {
		List<CommentModel> myList = new ArrayList<>();

		for (Comment comment : dbCommentList) {
			Long commentId = comment.getId();
			CommentModel cm = new CommentModel();

			if (!userDaoService.getLastAvatar(userId).isEmpty())
				cm.setAuthorAvatar(userDaoService.getLastAvatar(userId).get(0).getUrl());

			cm.setAuthorFirstName(comment.getUser().getFirstName());
			cm.setAuthorLastName(comment.getUser().getLastName());
			cm.setCreationDate(comment.getDatetimeCreated());
			cm.setId(commentId);
			cm.setImageLink(comment.getImageLink());
			cm.setMyComment(comment.getUser().getId() == userId);

			boolean isCommentLikedByMe = isContentLikedByUser(comment, userId);
			cm.setLikedByUser(isCommentLikedByMe);
			cm.setLikeCount(comment.getLikes().size());

			List<EditHistory> editHistory = editContentDaoService.getLastEditedComent(commentId);
			if (!editHistory.isEmpty()) {
				cm.setEdited(true);

				EditHistory ec = editHistory.get(0);

				cm.setCreationDate(ec.getDatetimeEdited());
				cm.setText(ec.getText());
			} else {
				cm.setText(comment.getContent());
				cm.setCreationDate(comment.getDatetimeCreated());
			}

			myList.add(cm);
		}

		return myList;

	}

	// detect if post or comment has already been liked by user
	protected boolean isContentLikedByUser(Object o, Long userId) {

		List<LikeContent> lp = new ArrayList<>();
		Post p = null;
		Comment c = null;

		if (o instanceof Post) {
			p = (Post) o;
			lp.addAll(p.getLikes());
		} else if (o instanceof Comment) {
			c = (Comment) o;
			lp.addAll(c.getLikes());
		}

		for (LikeContent likePost : lp) {
			if (likePost.getUser().getId() == userId) {
				return true;
			}
		}

		return false;

	}

}
