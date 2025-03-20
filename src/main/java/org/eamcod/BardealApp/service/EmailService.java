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

    @Value("${spring.mail.password}")
    private String fromPassword;

    private final String TO_EMAIL =  "emile@casyri.nl";


    public void sendAlarmEmail(AlarmIntake alarmIntake) {
        System.out.println("email: " + fromEmail);
        System.out.println("password: " + fromPassword);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(TO_EMAIL);
        message.setSubject(("Alarm Intake - " + alarmIntake.getCompanyName()));

        String body = "Ingekomen alarmintake:\n\n"
                + "Tijdstip: " + alarmIntake.getTimestamp() + "\n"
                + "Bericht: " + alarmIntake.getText();

        message.setText(body);
        mailSender.send(message);
        System.out.println("Email sent!");


    }
}
