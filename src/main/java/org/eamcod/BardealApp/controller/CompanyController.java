package org.eamcod.BardealApp.controller;

import org.eamcod.BardealApp.model.Company;
import org.eamcod.BardealApp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/companies")
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

//    @PutMapping("/my-company")
//    private ResponseEntity<Company> updateCompanyByLoggedInUser(@AuthenticationPrincipal OAuth2User principal){
//        Long companyId = userService.getCurrentUser(principal).getCompany().getId();
//        return new ResponseEntity<>( companyService.getCompanyById(companyId), HttpStatus.OK);
//    }

    @GetMapping("")
    private ResponseEntity<List<Company>> getAllCompanies() {
        return new ResponseEntity<>(userService.getAllCompanies(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    private ResponseEntity<Company> getCompany(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getCompany(id), HttpStatus.OK);
    }
}