package org.eamcod.BardealApp.service;

import org.eamcod.BardealApp.dto.CompanyOutputDTO;
import org.eamcod.BardealApp.model.AuthorityRole;
import org.eamcod.BardealApp.model.Company;
import org.eamcod.BardealApp.model.User;
import org.eamcod.BardealApp.repo.CompanyRepo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CompanyService {

    private final CompanyRepo companyRepo;
    private final UserService userService;

    public CompanyService(CompanyRepo companyRepo, UserService userService) {
        this.companyRepo = companyRepo;
        this.userService = userService;
    }

    public CompanyOutputDTO getSingleCompany(Long id) {
        Company company = companyRepo.findById(id).orElseThrow(() -> new NoSuchElementException("No company found with id: " + id));
        return companyToDTO(company);
    }

    public CompanyOutputDTO getSingleCompanyByName(String name) {
        Company company = companyRepo.findByName(name).orElseThrow(() -> new NoSuchElementException("No company found with username: " + name));
        return companyToDTO(company);
    }

    public List<CompanyOutputDTO> getAllCompanies(OAuth2User principal) throws AccessDeniedException {
        User currentUser = userService.getCurrentUser(principal);
        if(currentUser.getRole().equals(AuthorityRole.ADMIN)) {
            return companyRepo.findAll().stream()
                    .map(this::companyToDTO)
                    .toList();
        }
        if(currentUser.getRole().equals(AuthorityRole.MANAGER)) {
            return List.of(companyToDTO(currentUser.getCompany()));
        }
        throw new AccessDeniedException("Not authorized.");
    }




    public CompanyOutputDTO addCompany(Company company) {
        try {
            return companyToDTO(companyRepo.save(company));
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Company username and email should be unique.");
        }
    }

    public void delete(Long id) {
        companyRepo.deleteById(id);
    }

    public CompanyOutputDTO update(Long id, Company company) {
        Company oldCompany = companyRepo.findById(id).orElseThrow(() -> new NoSuchElementException("No company found with id: " + id));
        oldCompany.setName(company.getName());
        oldCompany.setEmail(company.getEmail());
        try {
            return companyToDTO(companyRepo.save(oldCompany));
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Company username and email should be unique.");
        }
    }

    public CompanyOutputDTO updateMyCompany(Long companyId, Company company) {
        Company oldCompany = companyRepo.findById(companyId).orElseThrow(() -> new NoSuchElementException("No company found with id: " + companyId));
        oldCompany.setEmail(company.getEmail());
        try {
            return companyToDTO(companyRepo.save(oldCompany));
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Company email should be unique.");
        }
    }

    private CompanyOutputDTO companyToDTO(Company company) {
        CompanyOutputDTO dto = new CompanyOutputDTO();
        dto.setId(company.getId());
        dto.setName(company.getName());
        dto.setEmail(company.getEmail());

        return dto;
    }


}
