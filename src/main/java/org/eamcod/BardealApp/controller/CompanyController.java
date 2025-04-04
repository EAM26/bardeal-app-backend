package org.eamcod.BardealApp.controller;

import org.eamcod.BardealApp.model.Company;
import org.eamcod.BardealApp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyController {

    private final UserService userService;
    private final CompanyService companyService;

    public CompanyController(UserService userService, CompanyService companyService) {
        this.userService = userService;
        this.companyService = companyService;
    }

    @GetMapping("/my-company")
    private ResponseEntity<Company> getCompanyByLoggedInUser(@AuthenticationPrincipal OAuth2User principal){
        Long companyId = userService.getCurrentUser(principal).getCompany().getId();
        return new ResponseEntity<>( companyService.getCompanyById(companyId), HttpStatus.OK);


    }
}