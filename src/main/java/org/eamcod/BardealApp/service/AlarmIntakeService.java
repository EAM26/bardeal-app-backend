package org.eamcod.BardealApp.service;

import org.eamcod.BardealApp.dto.AlarmIntakeInputDTO;
import org.eamcod.BardealApp.model.AlarmIntake;
import org.eamcod.BardealApp.model.User;
import org.eamcod.BardealApp.repo.AlarmIntakeRepo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class AlarmIntakeService {

    private final AlarmIntakeRepo alarmIntakeRepo;
    private final UserService userService;

    public AlarmIntakeService(AlarmIntakeRepo alarmIntakeRepo, UserService userService) {
        this.alarmIntakeRepo = alarmIntakeRepo;
        this.userService = userService;
    }

    public List<AlarmIntake> getAllForms() {
        return alarmIntakeRepo.findAll();
    }

    public AlarmIntake addForm(AlarmIntakeInputDTO inputDTO, MultipartFile pdfFile, @AuthenticationPrincipal OAuth2User principal) throws IOException {
        return alarmIntakeRepo.save(dtoToAlarmIntake(inputDTO, pdfFile, principal));


    }

    public AlarmIntake dtoToAlarmIntake(AlarmIntakeInputDTO dto, MultipartFile pdfFile, OAuth2User principal) throws IOException {
        AlarmIntake alarmIntake = new AlarmIntake();
        alarmIntake.setClientName(dto.getClientName());
        alarmIntake.setTimestamp(dto.getTimestamp());
        alarmIntake.setText(dto.getText());
        User currentUser = userService.getCurrentUser(principal);
        alarmIntake.setCompany(currentUser.getCompany());
        alarmIntake.setClientName(dto.getClientName());
        alarmIntake.setFileType(pdfFile.getContentType());
        alarmIntake.setFileData(pdfFile.getBytes());

        return alarmIntake;
    }
}
