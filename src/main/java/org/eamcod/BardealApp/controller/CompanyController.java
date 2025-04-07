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
    private ResponseEntity<Company> getOwnCompany(@AuthenticationPrincipal OAuth2User principal) {
        Long companyId = userService.getCurrentUser(principal).getCompany().getId();
        return new ResponseEntity<>(companyService.getCompany(companyId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    private ResponseEntity<Company> getSingleCompany(@PathVariable Long id) {
        return new ResponseEntity<>(companyService.getCompany(id), HttpStatus.OK);
    }

    @GetMapping("")
    private ResponseEntity<List<Company>> getAllCompanies() {
        return new ResponseEntity<>(companyService.getAllCompanies(), HttpStatus.OK);
    }

    @PostMapping("")
    private ResponseEntity<?> addCompany(@RequestBody Company company) {
        try {
            return new ResponseEntity<>(companyService.addCompany(company), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }


}