package org.eamcod.BardealApp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.eamcod.BardealApp.dto.AlarmIntakeInputDTO;
import org.eamcod.BardealApp.model.AlarmIntake;
import org.eamcod.BardealApp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private final UserService userService;

    @Value("${spring.mail.username}")
    private String fromEmail;

//    @Value("${email.recipient}")
//    private String emailRecipient;

    public EmailService(UserService userService) {
        this.userService = userService;
    }

    public void sendAlarmEmail(AlarmIntake alarmIntake, OAuth2User principal) throws MessagingException {

        User currentUser = userService.getCurrentUser(principal);
        String emailRecipient = currentUser.getCompany().getEmail();

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom(fromEmail);
        helper.setTo(emailRecipient);
        helper.setSubject(("Alarm Intake - " + alarmIntake.getClientName()));

        String body = "Ingekomen alarmintake:\n\n"
                + "Client naam: " + alarmIntake.getClientName() + "\n"
                + "Tijdstip: " + alarmIntake.getTimestamp() + "\n"
                + "Bericht: " + alarmIntake.getText();

        helper.setText(body);

        if (alarmIntake.getFileData() != null) {
            String name = String.format("AlarmIntake-%s-%s.pdf", alarmIntake.getClientName(), alarmIntake.getTimestamp());
            helper.addAttachment(name,
                    new org.springframework.core.io.ByteArrayResource(alarmIntake.getFileData()));
        }

        mailSender.send(mimeMessage);
        System.out.println("mail sent");

    }

    public boolean sendAlarmEmail(AlarmIntakeInputDTO dto, OAuth2User principal, MultipartFile pdfFile) throws MessagingException, IOException {

        User currentUser = userService.getCurrentUser(principal);
        String emailRecipient = currentUser.getCompany().getEmail();

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom(fromEmail);
        helper.setTo(emailRecipient);
        helper.setSubject(("Alarm Intake - " + dto.getClientName()));

        String body = "Ingekomen alarmintake:\n\n"
                + "Client naam: " + dto.getClientName() + "\n"
                + "Tijdstip: " + dto.getTimestamp() + "\n"
                + "Bericht: " + dto.getText();

        helper.setText(body);

        if (pdfFile != null && !pdfFile.isEmpty()) {
            String name = String.format("AlarmIntake-%s-%s.pdf", dto.getClientName(), dto.getTimestamp());
            helper.addAttachment(name,
                    new org.springframework.core.io.ByteArrayResource(pdfFile.getBytes()));
        }

        mailSender.send(mimeMessage);
        return true;

    }
}
