package pl.edu.agh.im.remotepatientmonitor.auth;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pl.edu.agh.im.remotepatientmonitor.domain.ApplicationUser;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    void sendActivationMessage(ApplicationUser user, String token) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
        mimeMessage.setContent(String.format("<html>\n" +
                "<body>\n" +
                "\n" +
                "Good morning %s,<br /><br />\n" +
                "Click this link to activate your account: <a href=\"%s\">Activate</a><br /><br />\n" +
                "Best regards,<br />\n" +
                "RPM Team\n" +
                "\n" +
                "</body>\n" +
                "</html>", user.getEmail(), String.format("http://localhost:8080/user/activate?token=%s", token)), "text/html");
        helper.setTo(user.getEmail());
        helper.setSubject("[RPM] - Account activation");
        emailSender.send(mimeMessage);
    }

    public void sendAlert(ApplicationUser user, String alert) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");
        message.setContent(String.format("<html>\n" +
                "<body>\n" +
                "\n" +
                "Good morning %s,<br /><br />\n" +
                alert + "<br />\n" +
                "Best regards,<br />\n" +
                "RPM Team\n" +
                "\n" +
                "</body>\n" +
                "</html>", user.getFullName()), "text/html");
        helper.setTo(user.getEmail());
        helper.setSubject("[RPM] - Heart rate exceeded");
        emailSender.send(message);
    }
}
