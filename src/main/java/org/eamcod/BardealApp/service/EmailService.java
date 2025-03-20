package org.eamcod.BardealApp.service;

import org.eamcod.BardealApp.model.AlarmIntake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${email.recipient}")
    private  String emailRecipient;

    public void sendAlarmEmail(AlarmIntake alarmIntake) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(emailRecipient);
        message.setSubject(("Alarm Intake - " + alarmIntake.getCompanyName()));

        String body = "Ingekomen alarmintake:\n\n"
                + "Tijdstip: " + alarmIntake.getTimestamp() + "\n"
                + "Bericht: " + alarmIntake.getText();

        message.setText(body);
        mailSender.send(message);


    }
}
