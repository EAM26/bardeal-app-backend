package org.eamcod.BardealApp.dto;

import lombok.Data;

@Data
public class CompanyOutputDTO {
    private Long id;
    private String name;
    private String email;
    private String address;
    private String zipcode;
    private String city;
    private String phoneNumber;

}
