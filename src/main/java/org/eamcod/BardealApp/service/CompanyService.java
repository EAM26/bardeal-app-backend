package org.eamcod.BardealApp.service;

import org.eamcod.BardealApp.dto.CompanyInputDTO;
import org.eamcod.BardealApp.dto.CompanyOutputDTO;
import org.eamcod.BardealApp.exception.CompanyNotFoundException;
import org.eamcod.BardealApp.model.AuthorityRole;
import org.eamcod.BardealApp.model.Company;
import org.eamcod.BardealApp.model.User;
import org.eamcod.BardealApp.repo.CompanyRepo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    private final CompanyRepo companyRepo;
    private final UserService userService;

    public CompanyService(CompanyRepo companyRepo, UserService userService) {
        this.companyRepo = companyRepo;
        this.userService = userService;
    }

    public CompanyOutputDTO getSingleCompany(Long id) {
        Company company = companyRepo.findById(id).orElseThrow(() -> new CompanyNotFoundException(id));
        return companyToDTO(company);
    }

    public CompanyOutputDTO getSingleCompanyByName(String name) {
        Company company = companyRepo.findByName(name).orElseThrow(() -> new CompanyNotFoundException(name));
        return companyToDTO(company);
    }

//    public List<CompanyOutputDTO> getAllCompanies(OAuth2User principal) throws AccessDeniedException {
//        User currentUser = userService.getCurrentUser(principal);
//        if(currentUser.getRole().equals(AuthorityRole.ADMIN)) {
//            return companyRepo.findAll().stream()
//                    .map(this::companyToDTO)
//                    .toList();
//        }
//        if(currentUser.getRole().equals(AuthorityRole.MANAGER)) {
//            return List.of(companyToDTO(currentUser.getCompany()));
//        }
//        throw new AccessDeniedException("Not authorized.");
//    }

    public List<CompanyOutputDTO> getAllCompanies() {
        return  companyRepo.findAll().stream()
                .map(this::companyToDTO)
                .toList();
    }




    public CompanyOutputDTO addCompany(CompanyInputDTO companyInputDTO) {
        try {
            Company company = companyRepo.save(dtoToCompany(companyInputDTO));
            return companyToDTO(company);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Company username and email should be unique.");
        }
    }

    public CompanyOutputDTO update(Long id, CompanyInputDTO companyInputDTO) {
        Company company = companyRepo.findById(id).orElseThrow(() -> new CompanyNotFoundException(id));
        try {
            company.setName(companyInputDTO.getName());
            company.setEmail(companyInputDTO.getEmail());
            company.setAddress(companyInputDTO.getAddress());
            company.setPhoneNumber(companyInputDTO.getPhoneNumber());
            company.setZipcode(companyInputDTO.getZipcode());
            company.setCity(companyInputDTO.getCity());
            return companyToDTO(companyRepo.save(company));
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Company username and email should be unique.");
        }
    }

    public void delete(Long id) {
        companyRepo.deleteById(id);
    }

//    public CompanyOutputDTO updateMyCompany(Long companyId, Company company) {
//        Company oldCompany = companyRepo.findById(companyId).orElseThrow(() -> new CompanyNotFoundException(companyId));
//        oldCompany.setEmail(company.getEmail());
//        try {
//            return companyToDTO(companyRepo.save(oldCompany));
//        } catch (DataIntegrityViolationException e) {
//            throw new IllegalArgumentException("Company email should be unique.");
//        }
//    }

    private CompanyOutputDTO companyToDTO(Company company) {
        CompanyOutputDTO dto = new CompanyOutputDTO();
        dto.setId(company.getId());
        dto.setName(company.getName());
        dto.setEmail(company.getEmail());
        dto.setAddress(company.getAddress());
        dto.setZipcode(company.getZipcode());
        dto.setCity(company.getCity());
        dto.setPhoneNumber(company.getPhoneNumber());
        return dto;
    }

    private Company dtoToCompany(CompanyInputDTO dto) {
        Company company = new Company();
        company.setName(dto.getName());
        company.setEmail(dto.getEmail());
        company.setAddress(dto.getAddress());
        company.setZipcode(dto.getZipcode());
        company.setCity(dto.getCity());
        company.setPhoneNumber(dto.getPhoneNumber());

        return company;
    }


}
