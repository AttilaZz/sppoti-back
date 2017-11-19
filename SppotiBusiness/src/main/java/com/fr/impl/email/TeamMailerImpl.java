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
import java.util.stream.Collectors;

/**
 * Created by djenanewail on 3/23/17.
 */
@Component
public class TeamMailerImpl extends ApplicationMailerServiceImpl implements TeamMailerService
{
	
	@Value("${spring.app.mail.team.add.subject}")
	private String addTeamSubject;
	
	@Value("${spring.app.mail.team.join.subject}")
	private String joinTeamSubject;
	
	@Value("${spring.app.mail.team.confirm.subject}")
	private String confirmJoinTeamSubject;
	
	@Value("${spring.app.mail.team.join.link}")
	private String joinTeamLink;
	
	@Value("${spring.app.mail.team.description}")
	private String sppotiTeamConcept;
	
	@Value("${spring.app.mail.team.invited.by.join.team}")
	private String toJoinTeamMessage;
	
	/** Sppoti email templates */
	private final static String PATH_TO_JOIN_TEAM_TEMPLATE = "team/join_team";
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@Autowired
	private UserParamService userParamService;
	
	/**
	 * Send email to confirm team creation.
	 *
	 * @param team
	 * 		ceated team.
	 */
	@Override
	public void sendAddTeamEmail(final TeamDTO team)
	{
		if (this.userParamService.canReceiveEmail()) {
		
		}
	}
	
	/**
	 * Send email to the team members.
	 *
	 * @param to
	 * 		team memeber.
	 * @param from
	 * 		team admin.
	 * @param team
	 * 		team data.
	 */
	@Override
	public void sendJoinTeamEmail(final TeamDTO team, final UserDTO to, final UserDTO from)
	{
		if (this.userParamService.canReceiveEmail()) {
			final String joinTeamLinkParsed = this.frontRootPath +
					this.joinTeamLink.replace("%teamId%", team.getId() + "");
			prepareAndSendEmail(to, from, team, this.joinTeamSubject, joinTeamLinkParsed, buildTeamMailResources());
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
	
	/**
	 * Send email to confirm team join.
	 *
	 * @param team
	 * 		team data.
	 * @param member
	 * 		team member.
	 */
	@Override
	public void sendConfirmJoinTeamEmail(final TeamDTO team, final TeamMemberEntity member)
	{
		if (this.userParamService.canReceiveEmail()) {
		
		}
		
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
		
		final Context context = new Context();
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
		
		context.setVariable("globalInformationAboutTeams", this.sppotiTeamConcept);
		context.setVariable("learnMoreMessage", this.learnMoreMessage);
		context.setVariable("joinMessage", this.joinMessage);
		context.setVariable("invitedByMessage", this.invitedByMessage);
		context.setVariable("toJoinTeamMessage", this.toJoinTeamMessage);
		context.setVariable("andPrepositionMessage", this.andPrepositionMessage);
		context.setVariable("otherPrepositionMessage", this.otherPrepositionMessage);
		
		//Template footer.
		context.setVariable("emailIntendedForMessageText", this.emailIntendedForMessage);
		context.setVariable("notYourAccountMessageText", this.notYourAccountMessage);
		context.setVariable("contactUsMessageText", this.contactUsMessage);
		context.setVariable("contactUsLink", this.contactUsLink);
		context.setVariable("sentToText", this.sentToTextMessage);
		
		final String text = this.templateEngine.process(PATH_TO_JOIN_TEAM_TEMPLATE, context);
		
		super.prepareAndSendEmail(to.getEmail(), subject, text, resourceContent);
	}
}
