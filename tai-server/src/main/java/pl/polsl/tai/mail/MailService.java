package pl.polsl.tai.mail;

import org.thymeleaf.context.Context;

public interface MailService {
  void send(String to, String subject, Context context, MailTemplate template);
}
