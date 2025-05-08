package org.eamcod.BardealApp.controller;

import org.eamcod.BardealApp.dto.CompanyInputDTO;
import org.eamcod.BardealApp.dto.CompanyOutputDTO;
import org.eamcod.BardealApp.exception.CompanyHasUserException;
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
    public ResponseEntity<?> getAllCompanies(@AuthenticationPrincipal OAuth2User principal) {
        try {

            return new ResponseEntity<>(companyService.getAllCompanies(principal), HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSingleCompany(@AuthenticationPrincipal OAuth2User principal, @PathVariable Long id) {
        System.out.println("Get single company running...");
        try {
            return new ResponseEntity<>(companyService.getSingleCompany(id, principal), HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
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
    public ResponseEntity<?> addCompany(@RequestBody CompanyInputDTO companyInputDTO) {
        try {
            return new ResponseEntity<>(companyService.addCompany(companyInputDTO), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompany(@PathVariable Long id, @RequestBody CompanyInputDTO companyInputDTO,
                                          @AuthenticationPrincipal OAuth2User principal) {
        System.out.println("update company running....");
        try {
            return new ResponseEntity<>(companyService.update(id, companyInputDTO, principal), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable Long id) {
        try {
            companyService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (CompanyHasUserException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }




//    @GetMapping("/my-company")
//    public ResponseEntity<CompanyOutputDTO> getOwnCompany(@AuthenticationPrincipal OAuth2User principal) throws AccessDeniedException {
//        Long companyId = userService.getCurrentUser(principal).getCompany().getId();
//        return new ResponseEntity<>(companyService.getSingleCompany(companyId, principal), HttpStatus.OK);
//    }

    @PutMapping("/my-company")
    private ResponseEntity<?> updateMyCompany(@AuthenticationPrincipal OAuth2User principal, @RequestBody CompanyInputDTO companyInputDTO) {
        Long companyId = userService.getCurrentUser(principal).getCompany().getId();
        try {
//            return new ResponseEntity<>(companyService.updateMyCompany(companyId, company), HttpStatus.OK);
            return new ResponseEntity<>(companyService.update(companyId, companyInputDTO, principal), HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}