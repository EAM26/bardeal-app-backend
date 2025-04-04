package org.eamcod.BardealApp.controller;

import org.eamcod.BardealApp.model.Company;
import org.eamcod.BardealApp.repo.CompanyRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class CompanyService {

    private final CompanyRepo companyRepo;

    public CompanyService(CompanyRepo companyRepo) {
        this.companyRepo = companyRepo;
    }

    public Company getCompanyById(Long companyId) {
        return companyRepo.findById(companyId).orElseThrow(()-> new NoSuchElementException("No company found with id: " + companyId));
    }
}
