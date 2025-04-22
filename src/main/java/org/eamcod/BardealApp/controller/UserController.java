package org.eamcod.BardealApp.controller;

import org.eamcod.BardealApp.dto.UserInputDTO;
import org.eamcod.BardealApp.dto.UserOutputDTO;
import org.eamcod.BardealApp.model.User;
import org.eamcod.BardealApp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(@AuthenticationPrincipal OAuth2User principal) {
        try {
            return new ResponseEntity<>(userService.getAllUsers(principal), HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody UserInputDTO userInputDto, @AuthenticationPrincipal OAuth2User principal) {
        try {
            return new ResponseEntity<>(userService.addUser(userInputDto, principal), HttpStatus.CREATED);
        } catch (IllegalArgumentException | AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id, @AuthenticationPrincipal OAuth2User principal) {
        try {
            userService.deleteUser(id, principal);
            return new ResponseEntity<>("User deleted", HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
}
