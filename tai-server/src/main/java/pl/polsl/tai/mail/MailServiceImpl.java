package pl.polsl.tai.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.polsl.tai.exception.RestServerException;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
class MailServiceImpl implements MailService {
	private final JavaMailSender mailSender;
	private final TemplateEngine templateEngine;
	private final MailProperties mailProperties;

	@Override
	public void send(String to, String subject, Context context, MailTemplate template) {
		final MimeMessage message = mailSender.createMimeMessage();
		final String title = subject + " | System zarządzania biblioteką (TAI)";
		final String utcTimeNow = Instant.now()
			.atOffset(ZoneOffset.UTC)
			.format(DateTimeFormatter.ISO_INSTANT);

		context.setVariable("title", title);
		context.setVariable("now", utcTimeNow);

		try {
			final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
			final String htmlContent = templateEngine.process(template.getTemplateName(), context);

			mimeMessageHelper.setTo(to);
			mimeMessageHelper.setSubject(title);
			mimeMessageHelper.setText(htmlContent, true);
			mimeMessageHelper.setFrom(mailProperties.getFrom());

			mailSender.send(message);
		} catch (MessagingException ex) {
			throw new RestServerException();
		}
	}
}
