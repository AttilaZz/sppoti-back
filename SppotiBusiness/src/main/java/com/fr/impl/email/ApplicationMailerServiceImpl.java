package com.fr.impl.email;

import com.fr.commons.dto.MailResourceContent;
import com.fr.service.email.ApplicationMailerService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * Created by wdjenane on 09/02/2017.
 * <p>
 * Email super class configuration.
 **/
@Component
abstract class ApplicationMailerServiceImpl implements ApplicationMailerService
{
	
	/** Templates. */
	static final String PATH_TO_ACCOUNT_TEMPLATE = "account/account";
	static final String PATH_TO_CONTACT_TEMPLATE = "contact/contact";
	/** Email images directory. */
	static final String IMAGES_DIRECTORY = "templates/images/";
	/** name resources to use inside templates. */
	static final String logoResourceName = "sppoti_logo.png";
	static final String teamDefaultAvatarResourceName = "team_avatar.png";
	static final String sppotiCoverResourceName = "sppoti_bg.png";
	
	private static final String CHARSET_NAME = "UTF-8";
	
	private static final String ERROR_SENDING_MAIL = "Error sending email";
	private static final String ERROR_OPENING_RESOURCE_FILE = "Resource file not found";
	
	private static final String IMAGE_PNG = "image/png";
	
	protected static Logger LOGGER = LoggerFactory.getLogger(ApplicationMailerServiceImpl.class);
	
	@Autowired
	private JavaMailSender sender;
	@Autowired
	private MailProperties mailProperties;
	
	/** Front app path. */
	@Value("${spring.app.redirection.url}")
	protected String frontRootPath;
	
	/** Global email texts - for translation. */
	@Value("${spring.app.mail.intended.for}")
	String emailIntendedForMessage;
	@Value("${spring.app.mail.not.your.account}")
	String notYourAccountMessage;
	@Value("${spring.app.mail.contact.us.link}")
	String contactUsLink;
	@Value("${spring.app.mail.contact.us}")
	String contactUsMessage;
	@Value("${spring.app.mail.sent.to}")
	String sentToTextMessage;
	@Value("${spring.app.mail.learn.more}")
	String learnMoreMessage;
	@Value("${spring.app.mail.join}")
	String joinMessage;
	@Value("${spring.app.mail.invited.by}")
	String invitedByMessage;
	@Value("${spring.app.mail.and.preposition}")
	String andPrepositionMessage;
	@Value("${spring.app.mail.other.preposition}")
	String otherPrepositionMessage;
	
	/**
	 * @param to
	 * 		email receiver.
	 * @param subject
	 * 		email subject.
	 * @param content
	 * 		email content.
	 */
	protected void prepareAndSendEmail(final String to, final String subject, final String content,
									   final List<MailResourceContent> resourceContent)
	{
		final Thread thread = new Thread(() -> {
			final MimeMessage mail = this.sender.createMimeMessage();
			
			try {
				final MimeMessageHelper helper = new MimeMessageHelper(mail, true, CHARSET_NAME);
				
				helper.setFrom(this.mailProperties.getFrom());
				helper.setTo(to);
				helper.setSubject(subject);
				
				helper.setText(content, true);
				
				//add image resource
				if (Objects.nonNull(resourceContent)) {
					for (final MailResourceContent r : resourceContent) {
						// Add the inline image, referenced from the HTML code as "cid:${imageResourceName}"
						final InputStreamSource imageSource = getImageResource(r);
						helper.addInline(r.getResourceName(), imageSource, IMAGE_PNG);
					}
				}
				
				this.sender.send(mail);
				LOGGER.info("Email has been sent successfully sent to user: <{}>, with subject: <{}>", to, subject);
			} catch (final MessagingException | MailAuthenticationException e) {
				LOGGER.error(ERROR_SENDING_MAIL, e);
			} catch (final IOException e) {
				LOGGER.error(ERROR_OPENING_RESOURCE_FILE, e);
			}
		});
		thread.start();
	}
	
	private InputStreamSource getImageResource(final MailResourceContent resourceContent) throws IOException
	{
		//		final ClassLoader classLoader = getClass().getClassLoader();
		//		final File file = new File(classLoader.getResource(resourceContent.getPath()).getFile());
		
		final InputStream file = getClass().getResourceAsStream("/" + resourceContent.getPath());
		
		//		final byte[] imageBytes = Files.readAllBytes();
		final byte[] imageBytes = IOUtils.toByteArray(file);
		return new ByteArrayResource(imageBytes);
	}
}