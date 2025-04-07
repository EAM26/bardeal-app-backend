package org.eamcod.BardealApp.controller;

import org.eamcod.BardealApp.dto.UserInputDTO;
import org.eamcod.BardealApp.model.User;
import org.eamcod.BardealApp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody UserInputDTO userInputDto, @AuthenticationPrincipal OAuth2User principal)  {
        try {
            return new ResponseEntity<>(userService.addUser(userInputDto, principal), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }catch (AccessDeniedException e) {
            return new ResponseEntity<>("Access denied,", HttpStatus.FORBIDDEN);
        }
    }

}
