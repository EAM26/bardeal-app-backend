package org.eamcod.BardealApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String username;

    private String email;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(nullable = false)
    private Company company;

    @Enumerated(EnumType.STRING)
    private AuthorityRole role;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", company=" + company.getName() +
                ", role=" + role +
                '}';
    }
}


