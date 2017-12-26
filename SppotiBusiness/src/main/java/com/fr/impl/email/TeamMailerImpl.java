package com.fr.impl.email;

import com.fr.commons.dto.MailResourceContent;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.entities.TeamMemberEntity;
import com.fr.service.UserParamService;
import com.fr.service.email.TeamMailerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Created by djenanewail on 3/23/17.
 */
@Component
public class TeamMailerImpl extends ApplicationMailerServiceImpl implements TeamMailerService
{
	
	@Value("${mail.teamJoinLink}")
	private String joinTeamLink;
	
	private final static String PATH_TO_JOIN_TEAM_TEMPLATE = "team/team";
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@Autowired
	private UserParamService userParamService;
	
	@Override
	public void sendAddTeamEmail(final TeamDTO team)
	{
	}
	
	@Override
	public void sendJoinTeamEmail(final TeamDTO team, final UserDTO to, final UserDTO from)
	{
		if (this.userParamService.canReceiveEmail(to.getEmail())) {
			final String joinTeamLinkParsed = this.frontRootPath +
					this.joinTeamLink.replace("%teamId%", team.getId() + "");
			
			final String subject = this.messageSource
					.getMessage("mail.teamJoinSubject", null, Locale.forLanguageTag(to.getLanguage()));
			prepareAndSendEmail(to, from, team, subject, joinTeamLinkParsed, buildTeamMailResources());
		}
	}
	
	private List<MailResourceContent> buildTeamMailResources() {
		final List<MailResourceContent> resourceContents = new ArrayList<>();
		final MailResourceContent resourceContent = new MailResourceContent();
		resourceContent.setPath(IMAGES_DIRECTORY + teamDefaultAvatarResourceName);
		resourceContent.setResourceName(teamDefaultAvatarResourceName);
		resourceContents.add(resourceContent);
		return resourceContents;
	}
	
	
	@Override
	public void sendConfirmJoinTeamEmail(final TeamDTO team, final TeamMemberEntity member)
	{
	
	}
	
	/**
	 * Send email.
	 */
	private void prepareAndSendEmail(final UserDTO to, final UserDTO from, final TeamDTO team, final String subject,
									 final String joinTeamLink, final List<MailResourceContent> resourceContent)
	{
		final int memberCount = team.getMembers().size();
		final List<UserDTO> teamMembers = team.getMembers().stream().limit(2).collect(Collectors.toList());
		final int memberCountToDisplay = memberCount - teamMembers.size();
		
		final Context context = new Context(Locale.forLanguageTag(to.getLanguage()));
		context.setVariable("title", to.getFirstName());
		
		context.setVariable("sentFromName", from.getFirstName() + " " + from.getLastName());
		
		if (!StringUtils.isEmpty(joinTeamLink)) {
			context.setVariable("sentToValidationLink", joinTeamLink);
		}
		context.setVariable("sentToDiscoverLink", "");
		
		context.setVariable("sentToTeamName", team.getName());
		context.setVariable("sentToFirstName", to.getFirstName());
		context.setVariable("receiverEmail", to.getEmail());
		context.setVariable("receiverUsername", to.getUsername());
		
		context.setVariable("teamMembersCount", memberCountToDisplay);
		context.setVariable("teamMembers", teamMembers);
		
		//Team avatar
		context.setVariable("imageResourceName", resourceContent.get(0).getResourceName());
		
		//Template footer & header
		context.setVariable("contactUsLink", this.contactUsLink);
		
		final String text = this.templateEngine.process(PATH_TO_JOIN_TEAM_TEMPLATE, context);
		
		super.prepareAndSendEmail(to.getEmail(), subject, text, resourceContent);
	}
}
