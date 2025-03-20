package org.eamcod.BardealApp.service;

import org.eamcod.BardealApp.model.AlarmIntake;
import org.eamcod.BardealApp.repo.AlarmIntakeRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    public AlarmIntake addForm(AlarmIntake alarmIntake, MultipartFile pdfFile) throws IOException {
        alarmIntake.setFileType(pdfFile.getContentType());
        alarmIntake.setFileData(pdfFile.getBytes());
        return alarmIntakeRepo.save(alarmIntake);

    }
}
