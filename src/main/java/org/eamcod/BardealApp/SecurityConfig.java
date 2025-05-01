package org.eamcod.BardealApp;

import org.eamcod.BardealApp.model.User;
import org.eamcod.BardealApp.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UserRepo userRepo;

    @Value("${app.frontend.base-url}")
    private String frontendBaseUrl;

    @Bean
    public OidcUserService customOidcUserService() {
        return new OidcUserService() {
            @Override
            public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
                OidcUser oidcUser = super.loadUser(userRequest);
                String email = oidcUser.getEmail();

                // Fetch user from the database
                User user = userRepo.findByEmail(email)
                        .orElseThrow(() -> new OAuth2AuthenticationException("Unauthorized email"));

                // Build the roles
                List<GrantedAuthority> authorities =
                        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

                // Construct final user object
                return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo(), "email");
            }
        };
    }


@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                                .anyRequest().permitAll()
//                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//
//                        .requestMatchers(HttpMethod.GET, "/me").authenticated()
//
//                        .requestMatchers(HttpMethod.GET, "/alarm").hasAnyRole("ADMIN", "MANAGER")
//                        .requestMatchers(HttpMethod.POST, "/alarm").authenticated()
//                        .requestMatchers(HttpMethod.DELETE, "/alarm").hasAnyRole("ADMIN", "MANAGER")
//                        .requestMatchers(HttpMethod.PUT, "/alarm").hasAnyRole("ADMIN", "MANAGER")
//
//                        .requestMatchers(HttpMethod.GET, "/users").hasAnyRole("ADMIN", "MANAGER")
//                        .requestMatchers(HttpMethod.POST, "/users").hasAnyRole("ADMIN", "MANAGER")
//                        .requestMatchers(HttpMethod.DELETE, "/users").hasAnyRole("ADMIN", "MANAGER")
//
//                        .requestMatchers(HttpMethod.GET, "/companies/my-company").hasAnyRole("ADMIN", "MANAGER")
//                        .requestMatchers(HttpMethod.PUT, "/companies/my-company").hasAnyRole("ADMIN", "MANAGER")
//
//                        .requestMatchers(HttpMethod.GET, "/companies").hasAnyRole("ADMIN", "MANAGER")
//                        .requestMatchers(HttpMethod.GET, "/companies/**").hasAnyRole("ADMIN")
//
//                        .anyRequest().authenticated()
                )

                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )

                .oauth2Login(oauth2 -> oauth2
//                        .defaultSuccessUrl("http://217.123.94.45:5173", true)
//                        .defaultSuccessUrl("http://vrki.bardeal.nl:5173", true)
                        .defaultSuccessUrl(frontendBaseUrl, true)
                                .failureHandler(customOAuth2FailureHandler())
                                .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(customOidcUserService())
                        )
                )

                .logout(logout -> logout
//                        .logoutSuccessUrl("http://vrki.bardeal.nl:5173")
                        .logoutSuccessUrl(frontendBaseUrl)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(frontendBaseUrl));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationFailureHandler customOAuth2FailureHandler() {
        return (request, response, exception) -> {
            // Sessie opschonen
            request.getSession().invalidate();

            // Redirect naar jouw frontend-loginpagina (via property)
            response.sendRedirect(frontendBaseUrl + "/login?reason=oauth-failure");
        };
    }

}
