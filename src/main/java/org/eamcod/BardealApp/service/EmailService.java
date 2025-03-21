package org.eamcod.BardealApp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.eamcod.BardealApp.model.AlarmIntake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${email.recipient}")
    private String emailRecipient;

    public void sendAlarmEmail(AlarmIntake alarmIntake) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom(fromEmail);
        helper.setTo(emailRecipient);
        helper.setSubject(("Alarm Intake - " + alarmIntake.getCompanyName()));

        String body = "Ingekomen alarmintake:\n\n"
                + "Tijdstip: " + alarmIntake.getTimestamp() + "\n"
                + "Bericht: " + alarmIntake.getText();

        helper.setText(body);

        if (alarmIntake.getFileData() != null) {
            helper.addAttachment("intakeformulier.pdf",
                    new org.springframework.core.io.ByteArrayResource(alarmIntake.getFileData()));
        }

        mailSender.send(mimeMessage);
        System.out.println("mail sent");

    }
}
