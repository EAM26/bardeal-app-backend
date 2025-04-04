package org.eamcod.BardealApp.service;

import org.eamcod.BardealApp.model.AuthorityRole;
import org.eamcod.BardealApp.model.Company;
import org.eamcod.BardealApp.model.User;
import org.eamcod.BardealApp.repo.CompanyRepo;
import org.eamcod.BardealApp.repo.UserRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final CompanyRepo companyRepo;

    public UserService(UserRepo userRepo, CompanyRepo companyRepo) {
        this.userRepo = userRepo;
        this.companyRepo = companyRepo;
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User findByEmail(String email) {
        Optional<User> userOptional = userRepo.findByEmail(email);
        return userOptional.orElseThrow(() -> new NoSuchElementException("No user found with email: " + email));
    }

    public User addUser(User user) {
        return userRepo.save(user);
    }

    public User getCurrentUser(OAuth2User principal) {
        String email = principal.getAttribute("email");
        return findByEmail(email);

    }

    public Company getCompany(Long id) {
        return companyRepo.findById(id).orElseThrow(()-> new NoSuchElementException("No company found with id: " + id));
    }

    public List<Company> getAllCompanies() {
        return companyRepo.findAll();
    }
}
