package org.eamcod.BardealApp.dto;

import lombok.Data;
import org.eamcod.BardealApp.model.AuthorityRole;

@Data
public class UserOutputDTO {

    private Long id;
    private String username;
    private String email;
    private AuthorityRole role;
    private Long companyId;
    private String companyName;
}
