package org.eamcod.BardealApp.repo;

import org.eamcod.BardealApp.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepo extends JpaRepository<Company, Long> {
    boolean existsByName(String companyName);
    boolean existsByEmail(String companyEmail);

    @Query("SELECT u.company FROM User u WHERE u.id = :userId")
    Optional<Company> findCompanyByUserId(@Param("userId") Long userId);
    
    Optional<Company> findByName(String name);
}
