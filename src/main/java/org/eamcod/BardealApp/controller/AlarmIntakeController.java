package org.eamcod.BardealApp.controller;

import jakarta.mail.MessagingException;
import org.eamcod.BardealApp.service.AlarmIntakeService;
import org.eamcod.BardealApp.service.EmailService;
import org.eamcod.BardealApp.model.AlarmIntake;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/alarm")
@CrossOrigin
public class AlarmIntakeController {

    private final AlarmIntakeService alarmIntakeService;
    private final EmailService emailService;

    public AlarmIntakeController(AlarmIntakeService alarmIntakeService, EmailService emailService) {
        this.alarmIntakeService = alarmIntakeService;
        this.emailService = emailService;
    }

    @GetMapping()
    public ResponseEntity<List<AlarmIntake>> getAllForms() {
        System.out.println("Controller Running");
        return new ResponseEntity<>(alarmIntakeService.getAllForms(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> addForm(@RequestPart AlarmIntake alarmIntake, @RequestPart MultipartFile pdfFile, @AuthenticationPrincipal OAuth2User principal) {
        AlarmIntake savedAlarmIntake;
        try {
            savedAlarmIntake = alarmIntakeService.addForm(alarmIntake, pdfFile);
            emailService.sendAlarmEmail(savedAlarmIntake, principal);
            return new ResponseEntity<>(savedAlarmIntake, HttpStatus.CREATED);
        } catch(IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (MessagingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
