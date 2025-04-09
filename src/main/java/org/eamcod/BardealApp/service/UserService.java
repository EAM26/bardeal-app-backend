package org.eamcod.BardealApp.service;

import org.eamcod.BardealApp.dto.UserInputDTO;
import org.eamcod.BardealApp.dto.UserOutputDTO;
import org.eamcod.BardealApp.model.AuthorityRole;
import org.eamcod.BardealApp.model.Company;
import org.eamcod.BardealApp.model.User;
import org.eamcod.BardealApp.repo.CompanyRepo;
import org.eamcod.BardealApp.repo.UserRepo;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final CompanyService companyService;
    private final CompanyRepo companyRepo;

    public UserService(UserRepo userRepo, CompanyService companyService, CompanyRepo companyRepo) {
        this.userRepo = userRepo;
        this.companyService = companyService;
        this.companyRepo = companyRepo;
    }

    public List<UserOutputDTO> getAllUsers() {
        List<UserOutputDTO> users = userRepo.findAll().stream()
                .map(this::userToDto)
                .collect(Collectors.toList());
        return users;
    }

    public User findByEmail(String email) {
//        Optional<User> userOptional = userRepo.findByEmail(email);
//        return userOptional.orElseThrow(() -> new NoSuchElementException("No user found with email: " + email));
        return userRepo.findByEmail(email).orElseThrow(()-> new NoSuchElementException("No user found with email: " + email));
    }

    public UserOutputDTO addUser(UserInputDTO userInputDTO, OAuth2User principal) throws AccessDeniedException {
        User currentUser = getCurrentUser(principal);

//        Check role for Authority User
        if(currentUser.getRole() == AuthorityRole.USER) {
            throw new AccessDeniedException("No permission for role USER.");
        }

//        For role manager check if set company is own company
        if(currentUser.getRole() == AuthorityRole.MANAGER && !currentUser.getCompany().getId().equals(userInputDTO.getCompanyId())) {
            throw new AccessDeniedException("No permission for Manager to add to other company.");
        }

        //        For role manager check if new user is not Admin
        if(currentUser.getRole() == AuthorityRole.MANAGER && userInputDTO.getRole() == AuthorityRole.ADMIN) {
            throw new AccessDeniedException("No permission for MANAGER to set ADMIN.");
        }
        
        if(!companyRepo.existsById(userInputDTO.getCompanyId())) {
            throw new NoSuchElementException("No company found with id: " + userInputDTO.getCompanyId());
        }

        if(userRepo.existsByUsernameAndCompanyId(userInputDTO.getUsername(), userInputDTO.getCompanyId())) {
            throw new IllegalArgumentException("Name must be unique in company");
        }

        if(userRepo.existsByEmailAndCompanyId(userInputDTO.getEmail(), userInputDTO.getCompanyId())) {
            throw new IllegalArgumentException("Email must be unique in company");
        }

        User createdUser = userRepo.save(dtoToUser(userInputDTO));
        return userToDto(createdUser);
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

    public UserOutputDTO userToDto(User user) {
        UserOutputDTO dto = new UserOutputDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setCompanyId(user.getCompany().getId());
        dto.setCompanyName(user.getCompany().getName());

        return dto;
    }


    public void deleteUser(Long id, OAuth2User principal) throws AccessDeniedException {
        User currentUser = getCurrentUser(principal);
        User user = userRepo.findById(id).orElseThrow(()-> new NoSuchElementException("No user found with id: " + id));

        if(Objects.equals(currentUser.getId(), user.getId())) {
            throw new AccessDeniedException("Can't delete yourself.");
        }

        if(user.getCompany() != currentUser.getCompany() &&
        currentUser.getRole() == AuthorityRole.MANAGER) {
            throw new AccessDeniedException("No permission to delete user of other company.");
        }

        if(user.getRole() == AuthorityRole.ADMIN && currentUser.getRole() == AuthorityRole.MANAGER) {
            throw new AccessDeniedException("No permission to delete Admin");
        }

        userRepo.deleteById(id);

    }
}
