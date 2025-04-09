package org.eamcod.BardealApp.service;

import org.eamcod.BardealApp.model.Company;
import org.eamcod.BardealApp.repo.CompanyRepo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CompanyService {

    private final CompanyRepo companyRepo;

    public CompanyService(CompanyRepo companyRepo) {
        this.companyRepo = companyRepo;
    }

    public Company getSingleCompany(Long id) {
        return companyRepo.findById(id).orElseThrow(() -> new NoSuchElementException("No company found with id: " + id));
    }

    public Company getSingleCompanyByNam(String name) {
        return companyRepo.findByName(name).orElseThrow(() -> new NoSuchElementException("No company found with username: " + name));
    }

    public List<Company> getAllCompanies() {
        return companyRepo.findAll();
    }

    public Company addCompany(Company company) {
        try {
            return companyRepo.save(company);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Company username and email should be unique.");
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
            throw new IllegalArgumentException("Company username and email should be unique.");
        }
    }

    public Object updateMyCompany(Long companyId, Company company) {
        Company oldCompany = companyRepo.findById(companyId).orElseThrow(() -> new NoSuchElementException("No company found with id: " + companyId));
        oldCompany.setEmail(company.getEmail());
        try {
            return companyRepo.save(oldCompany);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Company email should be unique.");
        }
    }
}
