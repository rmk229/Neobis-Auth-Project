package neo.neobis_auth_project.config.senderConfig;

import org.thymeleaf.context.Context;

public interface EmailSenderService {
    void sendEmailWithHTMLTemplate(String to,String from,String subject, String template, Context context);
}