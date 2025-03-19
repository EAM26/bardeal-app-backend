package org.eamcod.BardealApp.controller;

import org.eamcod.BardealApp.AlarmIntakeService;
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

    public AlarmIntakeController(AlarmIntakeService alarmIntakeService) {
        this.alarmIntakeService = alarmIntakeService;
    }

    @GetMapping("/forms")
    public ResponseEntity<List<AlarmIntake>> getAllForms() {
        System.out.println("Controller Running");
        return new ResponseEntity<>(alarmIntakeService.getAllForms(), HttpStatus.OK);
    }

    @PostMapping("/form")
    public ResponseEntity<AlarmIntake> addForm(@RequestBody AlarmIntake alarmIntake) {
        System.out.println("Add Form");
        return new ResponseEntity<>(alarmIntakeService.addForm(alarmIntake), HttpStatus.OK);
    }


}
