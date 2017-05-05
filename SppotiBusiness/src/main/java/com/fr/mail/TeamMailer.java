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

/**
 * Created by djenanewail on 3/23/17.
 * <p>
 * Team mailer.
 */

@Component
public class TeamMailer extends ApplicationMailer
{
	
	@Value("${spring.app.mail.team.add.message}")
	private String addTeamMessage;
	@Value("${spring.app.mail.team.join.message}")
	private String joinTeamMessage;
	@Value("${spring.app.mail.team.confirm.message}")
	private String confirmJoinTeamMessage;
	
	@Value("${spring.app.mail.team.add.subject}")
	private String addTeamSubject;
	@Value("${spring.app.mail.team.join.subject}")
	private String joinTeamSubject;
	@Value("${spring.app.mail.team.confirm.subject}")
	private String confirmJoinTeamSubject;
	
	@Value("${spring.app.mail.team.join.link}")
	private String joinTeamLink;
	
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
		this.joinTeamLink = this.frontRootPath + this.joinTeamLink.replaceAll("(.*)%teamId%(.*)", team.getId() + "");
		prepareAndSendEmail(to, from, team, this.joinTeamSubject, this.joinTeamMessage, this.joinTeamLink, null);
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
		
		final Context context = new Context();
		context.setVariable("title", to.getFirstName());
		
		context.setVariable("sentFromName", from.getFirstName() + " " + from.getLastName());
		
		if (!StringUtils.isEmpty(joinTeamLink)) {
			context.setVariable("sentToValidationLink", joinTeamLink);
		}
		context.setVariable("sentToDiscoverLink", "");
		
		context.setVariable("body", content);
		context.setVariable("sentToTeamName", team.getName());
		context.setVariable("sentToTeamMembersCount", team.getMembers().size());
		context.setVariable("sentToFirstName", to.getFirstName());
		context.setVariable("sentToEmail", to.getEmail());
		context.setVariable("sentToUsername", to.getUsername());
		
		final String text = this.templateEngine.process(PATH_TO_TEAM_TEMPLATE, context);
		
		super.prepareAndSendEmail(to.getEmail(), subject, text, resourceContent);
	}
}
