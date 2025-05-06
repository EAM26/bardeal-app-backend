package org.eamcod.BardealApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String clientName;
    private LocalDateTime timestamp;
    private String text;
    private String fileType;

//    @ManyToOne
//    @JsonIgnore
////    private Company company;

    @Lob
    private byte[] fileData;

    @Override
    public String toString() {
        return "AlarmIntake{" +
                "id=" + id +
                ", clientName='" + clientName + '\'' +
                ", timestamp=" + timestamp +
                ", text='" + text + '\'' +
                '}';
    }
}
