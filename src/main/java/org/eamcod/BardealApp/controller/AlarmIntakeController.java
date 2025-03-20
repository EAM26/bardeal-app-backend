package org.eamcod.BardealApp.controller;

import org.eamcod.BardealApp.service.AlarmIntakeService;
import org.eamcod.BardealApp.service.EmailService;
import org.eamcod.BardealApp.model.AlarmIntake;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/form")
    public ResponseEntity<AlarmIntake> addForm(@RequestBody AlarmIntake alarmIntake) {
        AlarmIntake savedIntake = alarmIntakeService.addForm(alarmIntake);
        System.out.println("Add Form");
        emailService.sendAlarmEmail(savedIntake);
        return new ResponseEntity<>(savedIntake, HttpStatus.OK);
    }


}
