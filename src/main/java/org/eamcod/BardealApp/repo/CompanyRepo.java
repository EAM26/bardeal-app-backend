package org.eamcod.BardealApp.repo;

import org.eamcod.BardealApp.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepo extends JpaRepository<Company, Long> {
    boolean existsByName(String companyName);
    boolean existsByEmail(String companyEmail);


}
