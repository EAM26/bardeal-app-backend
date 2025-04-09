package org.eamcod.BardealApp.dto;


import lombok.Data;
import org.eamcod.BardealApp.model.Company;

import java.time.LocalDateTime;

@Data
public class AlarmIntakeInputDTO {

    private String clientName;
    private LocalDateTime timestamp;
    private String text;
    private String fileType;


    private Company company;

    private byte[] fileData;
}
