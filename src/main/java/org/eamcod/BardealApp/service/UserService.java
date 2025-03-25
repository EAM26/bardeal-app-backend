package org.eamcod.BardealApp.service;

import org.eamcod.BardealApp.model.User;
import org.eamcod.BardealApp.repo.UserRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
}
