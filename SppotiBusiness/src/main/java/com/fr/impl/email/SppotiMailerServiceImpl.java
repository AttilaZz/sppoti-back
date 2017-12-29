package com.fr.impl.email;

import com.fr.commons.dto.MailResourceContent;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.enums.SppotiResponse;
import com.fr.service.UserParamService;
import com.fr.service.email.SppotiMailerService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.fr.commons.enumeration.GlobalAppStatusEnum.CONFIRMED;

/**
 * Created by djenanewail on 3/23/17.
 */
@Component
public class SppotiMailerServiceImpl extends ApplicationMailerServiceImpl implements SppotiMailerService
{
	private final Logger LOGGER = LoggerFactory.getLogger(SppotiMailerServiceImpl.class);
	
	private final static String PATH_TO_SPPOTI_TEMPLATE = "sppoti/sppoti";
	
	@Value("${mail.sppotiJoinLink}")
	private String joinSppotiLink;
	
	@Autowired
	private TemplateEngine templateEngine;
	@Autowired
	private UserParamService userParamService;
	
	private Context context;
	
	@Override
	public void onCreateSppoti(final SppotiDTO sppoti, final UserDTO to, final UserDTO from)
	{
		this.LOGGER.info("Sending join sppoti email to {}", to.getUsername());
		if (!this.userParamService.canReceiveEmail(to.getUserId())) {
			this.LOGGER.info("{} has deactivated emails", to.getUsername());
			return;
		}
		
		this.context = new Context(Locale.forLanguageTag(to.getLanguage()));
		this.context.setVariable("requestFromSppotiAdmin", true);
		
		final Locale language = Locale.forLanguageTag(to.getLanguage());
		
		final String[] params = {from.getUsername()};
		final String content = this.messageSource.getMessage("mail.sppotiMessageToSppoter", params, language);
		this.context.setVariable("messageBody", content);
		
		final String joinSppotiLinkParsed = this.frontRootPath +
				this.joinSppotiLink.replace("%sppotiId%", sppoti.getId() + "");
		
		final String subject = this.messageSource.getMessage("mail.sppotiJoinSubject", null, language);
		
		prepareAndSendEmail(this.context, to, from, sppoti, subject, joinSppotiLinkParsed, PATH_TO_SPPOTI_TEMPLATE);
	}
	
	@Override
	public void onSendingJoinRequestToSppoti(final SppotiDTO sppoti, final UserDTO to, final UserDTO from)
	{
		this.LOGGER.info("Sending join sppoti email to sppoti admin: {}", to.getUsername());
		if (!this.userParamService.canReceiveEmail(to.getUserId())) {
			this.LOGGER.info("{} has deactivated emails", to.getUsername());
			return;
		}
		
		this.context = new Context(Locale.forLanguageTag(to.getLanguage()));
		this.context.setVariable("requestFromSppotiAdmin", false);
		
		final Locale language = Locale.forLanguageTag(to.getLanguage());
		
		final String[] params = {from.getUsername()};
		final String content = this.messageSource.getMessage("mail.sppotiMessageToAdmin", params, language);
		this.context.setVariable("messageBody", content);
		
		final String joinSppotiLinkParsed = this.frontRootPath +
				this.joinSppotiLink.replace("%sppotiId%", sppoti.getId() + "");
		
		final String subject = this.messageSource.getMessage("mail.sppotiJoinSubject", null, language);
		
		prepareAndSendEmail(this.context, to, from, sppoti, subject, joinSppotiLinkParsed, PATH_TO_SPPOTI_TEMPLATE);
	}
	
	//TODO: adapt this method to mailing plan
	@Override
	public void onRespondingToSppotiJoinRequest(final SppotiDTO sppoti, final UserDTO to, final UserDTO from,
												final SppotiResponse sppotiResponse)
	{
		this.LOGGER.info("Sending join sppoti response email to: {}", to.getUsername());
		if (!this.userParamService.canReceiveEmail(to.getUserId())) {
			this.LOGGER.info("{} has deactivated emails", to.getUsername());
			return;
		}
		
		this.context = new Context(Locale.forLanguageTag(to.getLanguage()));
		this.context.setVariable("requestFromSppotiAdmin", false);
		
		
		final Locale language = Locale.forLanguageTag(to.getLanguage());
		
		final String acceptanceStatus;
		if (sppotiResponse.equals(SppotiResponse.ACCEPTED)) {
			acceptanceStatus = this.messageSource.getMessage("mail.accepted", null, language);
		} else {
			acceptanceStatus = this.messageSource.getMessage("mail.refused", null, language);
		}
		
		final String[] params = {sppoti.getName(), acceptanceStatus};
		final String content = this.messageSource.getMessage("mail.sppotiMessageResponseJoinContent", params, language);
		this.context.setVariable("messageBody", content);
		
		final String subject = this.messageSource.getMessage("mail.sppotiJoinSubject", null, language);
		
		prepareAndSendEmail(this.context, to, from, sppoti, subject, null, PATH_TO_SPPOTI_TEMPLATE);
	}
	
	@Override
	public void onSppotiEdit(final SppotiDTO sppoti, final UserDTO from)
	{
		this.LOGGER.info("Sending email on sppoti edit ...");
		
		final List<UserDTO> sppotiMembersMailingList = getSppotiMailingList(sppoti);
		
		final Stream<UserDTO> mailingList = sppotiMembersMailingList.stream()
				.filter(m -> this.userParamService.canReceiveEmail(m.getUserId()));
		
		if (mailingList.count() == 0) {
			this.LOGGER.info("All sppoti users have deactivated emails");
		}
		
		mailingList.forEach(m -> {
			final Locale language = Locale.forLanguageTag(m.getLanguage());
			
			final String[] params = {from.getUsername()};
			final String content = this.messageSource.getMessage("mail.sppotiEditedContent", params, language);
			this.context.setVariable("messageBody", content);
			
			final String subject = this.messageSource.getMessage("mail.sppotiEditedSubject", null, language);
			
			prepareAndSendEmail(this.context, m, from, sppoti, subject, null, PATH_TO_SPPOTI_TEMPLATE);
		});
	}
	
	@Override
	public void onSppotiDeleted(final SppotiDTO sppoti, final UserDTO from)
	{
		this.LOGGER.info("Sending email on sppoti deleted ...");
		
		final List<UserDTO> sppotiMembersMailingList = getSppotiMailingList(sppoti);
		
		final Stream<UserDTO> mailingList = sppotiMembersMailingList.stream()
				.filter(m -> this.userParamService.canReceiveEmail(m.getUserId()));
		
		if (mailingList.count() == 0) {
			this.LOGGER.info("All sppoti users have deactivated emails");
		}
		
		mailingList.forEach(m -> {
			final Locale language = Locale.forLanguageTag(m.getLanguage());
			
			final String[] params = {from.getUsername()};
			final String content = this.messageSource.getMessage("mail.sppotiDeletedContent", params, language);
			this.context.setVariable("messageBody", content);
			
			final String subject = this.messageSource.getMessage("mail.sppotiDeletedSubject", null, language);
			
			prepareAndSendEmail(this.context, m, from, sppoti, subject, null, PATH_TO_SPPOTI_TEMPLATE);
		});
	}
	
	private List<UserDTO> getSppotiMailingList(final SppotiDTO sppoti) {
		final List<UserDTO> sppotiMembersMailingList = new ArrayList<>();
		
		final Optional<TeamDTO> adverseTeam = sppoti.getTeamAdverse().stream()
				.filter(t -> t.getTeamAdverseStatus().equals(CONFIRMED.name())).findFirst();
		
		adverseTeam.ifPresent(a -> {
			final List<UserDTO> teamAdverseMailingList = a.getMembers().stream()
					.filter(m -> m.getSppotiStatus().equals(CONFIRMED)).collect(Collectors.toList());
			
			sppotiMembersMailingList.addAll(teamAdverseMailingList);
		});
		
		final List<UserDTO> teamHostMailingList = sppoti.getTeamHost().getMembers().stream()
				.filter(m -> m.getSppotiStatus().equals(CONFIRMED)).collect(Collectors.toList());
		sppotiMembersMailingList.addAll(teamHostMailingList);
		return sppotiMembersMailingList;
	}
	
	private List<MailResourceContent> buildSppotiMailResources() {
		final List<MailResourceContent> resourceContents = new ArrayList<>();
		final MailResourceContent avatarResourceContent = new MailResourceContent(), coverResourceContent
				= new MailResourceContent();
		avatarResourceContent.setPath(IMAGES_DIRECTORY + TEAM_AVATAR_RESOURCE_NAME);
		avatarResourceContent.setResourceName(TEAM_AVATAR_RESOURCE_NAME);
		
		coverResourceContent.setPath(IMAGES_DIRECTORY + SPPOTI_COVER_RESOURCE_NAME);
		coverResourceContent.setResourceName(SPPOTI_COVER_RESOURCE_NAME);
		
		resourceContents.add(avatarResourceContent);
		resourceContents.add(coverResourceContent);
		return resourceContents;
	}
	
	/**
	 * Send email.
	 */
	private void prepareAndSendEmail(final Context context, final UserDTO to, final UserDTO from,
									 final SppotiDTO Sppoti, final String subject, final String joinSppotiLink,
									 final String templatePath)
	{
		context.setVariable("title", to.getFirstName());
		
		context.setVariable("sentFromName", from.getFirstName() + " " + from.getLastName());
		
		if (!StringUtils.isEmpty(joinSppotiLink)) {
			context.setVariable("sentToValidationLink", joinSppotiLink);
		}
		
		context.setVariable("sentToSppotiName", Sppoti.getName());
		context.setVariable("sentToFirstName", to.getFirstName());
		context.setVariable("sentToEmail", to.getEmail());
		context.setVariable("receiverUsername", to.getUsername());
		context.setVariable("receiverEmail", to.getEmail());
		
		final List<MailResourceContent> mailResourceContents = buildSppotiMailResources();
		context.setVariable("avatarResourceName", mailResourceContents.get(0).getResourceName());
		context.setVariable("coverResourceName", mailResourceContents.get(1).getResourceName());
		
		//Template footer & header
		context.setVariable("contactUsLink", this.contactUsLink);
		
		final String text = this.templateEngine.process(templatePath, context);
		
		super.prepareAndSendEmail(to.getEmail(), subject, text, mailResourceContents);
	}
	
}
