package org.eamcod.BardealApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmIntake {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String companyName;
    private LocalDateTime timestamp;
    private String text;
    private String fileType;

    @ManyToOne
    private Company company;

    @Lob
    private byte[] fileData;

    @Override
    public String toString() {
        return "AlarmIntake{" +
                "id=" + id +
                ", companyName='" + companyName + '\'' +
                ", timestamp=" + timestamp +
                ", text='" + text + '\'' +
                '}';
    }
}
