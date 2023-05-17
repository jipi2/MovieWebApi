package atm.webproject.movieSite.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService
{
    @Autowired
    private JavaMailSender _mailSender;

    @Value("${spring.mail.username}")
    private String _emailSender;

    public  void sendEmail(String toEmail,
                           String subject,
                           String body)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(_emailSender);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        _mailSender.send(message);
    }

}
