package org.eamcod.BardealApp.controller;

import org.eamcod.BardealApp.model.User;
import org.eamcod.BardealApp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.RestController;


import java.util.Map;

@RestController
@CrossOrigin
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
//        User user = userRepository.findByEmail(email);
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        System.out.println("************");
        System.out.println(user);
        System.out.println("************");

        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "name", user.getUsername(),
                "email", user.getEmail(),
                "role", user.getRole()
        ));
    }

}
