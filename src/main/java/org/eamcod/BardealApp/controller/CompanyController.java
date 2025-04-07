package org.eamcod.BardealApp.controller;

import org.eamcod.BardealApp.model.Company;
import org.eamcod.BardealApp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RequestMapping("/companies")
@RestController
public class CompanyController {

    private final UserService userService;
    private final CompanyService companyService;

    public CompanyController(UserService userService, CompanyService companyService) {
        this.userService = userService;
        this.companyService = companyService;
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

    @DeleteMapping("/{id}")
    private ResponseEntity<String> deleteCompany(@PathVariable Long id) {
        companyService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    private ResponseEntity<?> updateCompany(@PathVariable Long id, @RequestBody Company company) {
        try {
            return new ResponseEntity<>(companyService.update(id, company), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/my-company")
    private ResponseEntity<Company> getOwnCompany(@AuthenticationPrincipal OAuth2User principal) {
        Long companyId = userService.getCurrentUser(principal).getCompany().getId();
        return new ResponseEntity<>(companyService.getCompany(companyId), HttpStatus.OK);
    }

    @PutMapping("/my-company")
    private ResponseEntity<?> updateMyCompany(@AuthenticationPrincipal OAuth2User principal, @RequestBody Company company) {
        Long companyId = userService.getCurrentUser(principal).getCompany().getId();
        try {
            return new ResponseEntity<>(companyService.updateMyCompany(companyId, company), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }


}