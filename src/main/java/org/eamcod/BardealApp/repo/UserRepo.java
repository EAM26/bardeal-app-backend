package org.eamcod.BardealApp.repo;

import org.eamcod.BardealApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByUsernameAndCompanyId(String username, Long CompanyId);
    boolean existsByEmailAndCompanyId(String email, Long CompanyId);
}
