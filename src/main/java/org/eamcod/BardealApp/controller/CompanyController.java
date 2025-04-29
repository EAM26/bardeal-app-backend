package org.eamcod.BardealApp.controller;

import org.eamcod.BardealApp.dto.CompanyInputDTO;
import org.eamcod.BardealApp.dto.CompanyOutputDTO;
import org.eamcod.BardealApp.model.Company;
import org.eamcod.BardealApp.service.CompanyService;
import org.eamcod.BardealApp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
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

    @GetMapping("")
    private ResponseEntity<List<CompanyOutputDTO>> getAllCompanies() {
        return new ResponseEntity<>(companyService.getAllCompanies(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    private ResponseEntity<CompanyOutputDTO> getSingleCompany(@PathVariable Long id) {
        return new ResponseEntity<>(companyService.getSingleCompany(id), HttpStatus.OK);
    }

//    @GetMapping("")
//    private ResponseEntity<?> getAllCompanies(@AuthenticationPrincipal OAuth2User principal) {
//        try {
//            return new ResponseEntity<>(companyService.getAllCompanies(principal), HttpStatus.OK);
//        } catch (AccessDeniedException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
//        }
//    }


    @PostMapping("")
    private ResponseEntity<?> addCompany(@RequestBody CompanyInputDTO companyInputDTO) {
        try {
            return new ResponseEntity<>(companyService.addCompany(companyInputDTO), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{id}")
    private ResponseEntity<?> updateCompany(@PathVariable Long id, @RequestBody CompanyInputDTO companyInputDTO) {
        try {
            return new ResponseEntity<>(companyService.update(id, companyInputDTO), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<String> deleteCompany(@PathVariable Long id) {
        companyService.delete(id);
        return ResponseEntity.noContent().build();
    }


//    Requests for the Company of the logged-in user


    @GetMapping("/my-company")
    private ResponseEntity<CompanyOutputDTO> getOwnCompany(@AuthenticationPrincipal OAuth2User principal) {
        Long companyId = userService.getCurrentUser(principal).getCompany().getId();
        return new ResponseEntity<>(companyService.getSingleCompany(companyId), HttpStatus.OK);
    }

    @PutMapping("/my-company")
    private ResponseEntity<?> updateMyCompany(@AuthenticationPrincipal OAuth2User principal, @RequestBody CompanyInputDTO companyInputDTO) {
        Long companyId = userService.getCurrentUser(principal).getCompany().getId();
        try {
//            return new ResponseEntity<>(companyService.updateMyCompany(companyId, company), HttpStatus.OK);
            return new ResponseEntity<>(companyService.update(companyId, companyInputDTO), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}