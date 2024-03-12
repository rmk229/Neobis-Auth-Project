package neo.neobis_auth_project.config.senderConfig;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import neo.neobis_auth_project.exceptions.BadCredentialException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderConfig implements EmailSenderService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Override
    @SneakyThrows
    public void sendEmailWithHTMLTemplate(String to, String from, String subject, String template, Context context) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
        try {
            messageHelper.setTo(to);
            messageHelper.setFrom(from);
            messageHelper.setSubject(subject);
            String htmlContent = templateEngine.process(template, context);
            messageHelper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
            log.info("Method Send works !!!");
        } catch (MessagingException e) {
            throw new BadCredentialException("Hit the error : " + e.getMessage());
        }
    }
}