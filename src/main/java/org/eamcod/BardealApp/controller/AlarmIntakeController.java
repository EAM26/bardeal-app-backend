package org.eamcod.BardealApp.controller;

import org.eamcod.BardealApp.service.AlarmIntakeService;
import org.eamcod.BardealApp.service.EmailService;
import org.eamcod.BardealApp.model.AlarmIntake;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/alarm/intake")
@CrossOrigin
public class AlarmIntakeController {

    private final AlarmIntakeService alarmIntakeService;
    private final EmailService emailService;

    public AlarmIntakeController(AlarmIntakeService alarmIntakeService, EmailService emailService) {
        this.alarmIntakeService = alarmIntakeService;
        this.emailService = emailService;
    }

    @GetMapping("/forms")
    public ResponseEntity<List<AlarmIntake>> getAllForms() {
        System.out.println("Controller Running");
        return new ResponseEntity<>(alarmIntakeService.getAllForms(), HttpStatus.OK);
    }

//    @PostMapping("/form")
//    public ResponseEntity<AlarmIntake> addForm(@RequestBody AlarmIntake alarmIntake) {
//        AlarmIntake savedIntake = alarmIntakeService.addForm(alarmIntake);
//        emailService.sendAlarmEmail(savedIntake);
//        return new ResponseEntity<>(savedIntake, HttpStatus.OK);
//    }

    @PostMapping("/form")
    public ResponseEntity<?> addForm(@RequestPart AlarmIntake alarmIntake, @RequestPart MultipartFile pdfFile) {
        AlarmIntake savedAlarmIntake;
        try {
            savedAlarmIntake = alarmIntakeService.addForm(alarmIntake, pdfFile);
            return new ResponseEntity<>(savedAlarmIntake, HttpStatus.CREATED);
        } catch(IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
