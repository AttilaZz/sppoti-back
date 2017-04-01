package com.fr.mail;

import com.fr.commons.dto.team.TeamResponseDTO;
import com.fr.entities.TeamMemberEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

/**
 * Created by djenanewail on 3/23/17.
 *
 * Team mailer.
 */

@Component
public class TeamMailer extends ApplicationMailer{

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

    public TeamMailer(JavaMailSender sender, MailProperties mailProperties, TemplateEngine templateEngine) {
        super(sender, mailProperties, templateEngine);
    }

    /**
     * Send email to confirm team creation.
     * @param team ceated team.
     */
    public void sendAddTeamEmail(final TeamResponseDTO team){

    }

    /**
     * Send email to the team members.
     * @param member team memeber.
     * @param team team data.
     */
    public void sendJoinTeamEmail(final TeamResponseDTO team, final TeamMemberEntity member){

    }

    /**
     * Send email to confirm team join.
     *
     * @param team team data.
     * @param member team member.
     */
    public void sendConfirmJoinTeamEmail(final TeamResponseDTO team, final TeamMemberEntity member){

    }
}
