package org.eamcod.BardealApp.controller;

import org.apache.coyote.BadRequestException;
import org.eamcod.BardealApp.model.Company;
import org.eamcod.BardealApp.repo.CompanyRepo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepo companyRepo;

    public CompanyService(CompanyRepo companyRepo) {
        this.companyRepo = companyRepo;
    }

    public Company getCompany(Long id) {
        return companyRepo.findById(id).orElseThrow(() -> new NoSuchElementException("No company found with id: " + id));
    }

    public List<Company> getAllCompanies() {
        return companyRepo.findAll();
    }

    public Company addCompany(Company company) {
        try {
            return companyRepo.save(company);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Company name and email should be unique.");
        }
    }

    public void delete(Long id) {
        companyRepo.deleteById(id);
    }

    public Company update(Long id, Company company) {
        Company oldCompany = companyRepo.findById(id).orElseThrow(() -> new NoSuchElementException("No company found with id: " + id));
        oldCompany.setName(company.getName());
        oldCompany.setEmail(company.getEmail());
        try {
            return companyRepo.save(oldCompany);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Company name and email should be unique.");
        }
    }
}
