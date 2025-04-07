package org.eamcod.BardealApp.service;

import org.eamcod.BardealApp.dto.UserInputDTO;
import org.eamcod.BardealApp.model.AuthorityRole;
import org.eamcod.BardealApp.model.Company;
import org.eamcod.BardealApp.model.User;
import org.eamcod.BardealApp.repo.UserRepo;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final CompanyService companyService;

    public UserService(UserRepo userRepo, CompanyService companyService) {
        this.userRepo = userRepo;
        this.companyService = companyService;
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User findByEmail(String email) {
//        Optional<User> userOptional = userRepo.findByEmail(email);
//        return userOptional.orElseThrow(() -> new NoSuchElementException("No user found with email: " + email));
        return userRepo.findByEmail(email).orElseThrow(()-> new NoSuchElementException("No user found with email: " + email));
    }

    public User addUser(UserInputDTO userInputDTO, OAuth2User principal) throws AccessDeniedException {
        User currentUser = getCurrentUser(principal);

        if(currentUser.getRole() == AuthorityRole.USER) {
            throw new AccessDeniedException("No permission.");
        }

        if(currentUser.getRole() == AuthorityRole.MANAGER) {
            userInputDTO.setCompanyId(currentUser.getCompany().getId());
        }


        if(userRepo.existsByUsernameAndCompanyId(userInputDTO.getUsername(), userInputDTO.getCompanyId())) {
            throw new IllegalArgumentException("Name must be unique in company");
        }

        if(userRepo.existsByEmailAndCompanyId(userInputDTO.getEmail(), userInputDTO.getCompanyId())) {
            throw new IllegalArgumentException("Email must be unique in company");
        }



        return userRepo.save(dtoToUser(userInputDTO));
    }

    public User getCurrentUser(OAuth2User principal) {
        String email = principal.getAttribute("email");
        return findByEmail(email);

    }

    public User dtoToUser(UserInputDTO userInputDTO) {
        User user = new User();
        user.setUsername(userInputDTO.getUsername());
        user.setEmail(userInputDTO.getEmail());
        user.setRole(userInputDTO.getRole());

        Company company = companyService.getSingleCompany(userInputDTO.getCompanyId());
        user.setCompany(company);
        return user;
    }


}
