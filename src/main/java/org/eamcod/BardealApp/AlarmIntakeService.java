package org.eamcod.BardealApp;

import org.eamcod.BardealApp.model.AlarmIntake;
import org.eamcod.BardealApp.repo.AlarmIntakeRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlarmIntakeService {

    private final AlarmIntakeRepo alarmIntakeRepo;

    public AlarmIntakeService(AlarmIntakeRepo alarmIntakeRepo) {
        this.alarmIntakeRepo = alarmIntakeRepo;
    }

    public List<AlarmIntake> getAllForms() {
        return alarmIntakeRepo.findAll();
    }

    public AlarmIntake addForm(AlarmIntake alarmIntake) {
        return alarmIntakeRepo.save(alarmIntake);
    }
}
