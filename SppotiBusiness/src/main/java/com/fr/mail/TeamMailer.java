package com.fr.mail;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.entities.TeamMemberEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by djenanewail on 3/23/17.
 * <p>
 * Team mailer.
 */

@Component
public class TeamMailer extends ApplicationMailer
{
	
	/** Notify team admin about his new team. */
	@Value("${spring.app.mail.team.add.message}")
	private String addTeamMessage;
	@Value("${spring.app.mail.team.add.subject}")
	private String addTeamSubject;
	
	/** Notify team member about team invitation. */
	@Value("${spring.app.mail.team.join.message}")
	private String joinTeamMessage;
	@Value("${spring.app.mail.team.join.subject}")
	private String joinTeamSubject;
	
	/** Notify team admin if a member accept or refuse to join his team. */
	@Value("${spring.app.mail.team.confirm.message}")
	private String confirmJoinTeamMessage;
	@Value("${spring.app.mail.team.confirm.subject}")
	private String confirmJoinTeamSubject;
	
	/** Redirection link to the front app. */
	@Value("${spring.app.mail.team.join.link}")
	private String joinTeamLink;
	
	/** Explain team utility. */
	@Value("${spring.app.mail.team.explanation}")
	private String sppotiTeamConcept;
	
	@Value("${spring.app.mail.team.invited.by.join.team}")
	private String toJoinTeamMessage;
	
	/** Init team mailer. */
	public TeamMailer(final JavaMailSender sender, final MailProperties mailProperties,
					  final TemplateEngine templateEngine)
	{
		super(sender, mailProperties, templateEngine);
	}
	
	/**
	 * Send email to confirm team creation.
	 *
	 * @param team
	 * 		ceated team.
	 */
	public void sendAddTeamEmail(final TeamDTO team)
	{
	
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
	public void sendJoinTeamEmail(final TeamDTO team, final UserDTO to, final UserDTO from)
	{
		
		final ResourceContent resourceContent = new ResourceContent();
		resourceContent.setPath(IMAGES_DIRECTORY + teamDefaultAvatarResourceName);
		resourceContent.setResourceName(teamDefaultAvatarResourceName);
		
		final String joinTeamLinkParsed = this.frontRootPath + this.joinTeamLink.replace("%teamId%", team.getId() + "");
		prepareAndSendEmail(to, from, team, this.joinTeamSubject, this.joinTeamMessage, joinTeamLinkParsed,
				resourceContent);
	}
	
	/**
	 * Send email to confirm team join.
	 *
	 * @param team
	 * 		team data.
	 * @param member
	 * 		team member.
	 */
	public void sendConfirmJoinTeamEmail(final TeamDTO team, final TeamMemberEntity member)
	{
	
	}
	
	/**
	 * Send email.
	 */
	private void prepareAndSendEmail(final UserDTO to, final UserDTO from, final TeamDTO team, final String subject,
									 final String content, final String joinTeamLink,
									 final ResourceContent resourceContent)
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
		
		context.setVariable("body", content);
		context.setVariable("sentToTeamName", team.getName());
		context.setVariable("sentToFirstName", to.getFirstName());
		context.setVariable("receiverEmail", to.getEmail());
		context.setVariable("receiverUsername", to.getUsername());
		
		context.setVariable("teamMembersCount", memberCountToDisplay);
		context.setVariable("teamMembers", teamMembers);
		
		//Team avatar
		context.setVariable("imageResourceName", resourceContent.getResourceName());
		
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
		
		final String text = this.templateEngine.process(PATH_TO_TEAM_TEMPLATE, context);
		
		super.prepareAndSendEmail(to.getEmail(), subject, text, resourceContent);
	}
}
