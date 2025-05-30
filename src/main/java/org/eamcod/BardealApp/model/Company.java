package org.eamcod.BardealApp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String email;

    private String address;

    private String zipcode;

    private String city;

    private String phoneNumber;

    @OneToMany(mappedBy = "company")
    private List<User> users;

//    @OneToMany(mappedBy = "company")
//    private List<AlarmIntake> alarmIntakes;

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", username='" + name + '\'' +
                ", email='" + email + '\'' +
//                ", users=" + users +
//                ", alarmIntakes=" + alarmIntakes +
                '}';
    }
}