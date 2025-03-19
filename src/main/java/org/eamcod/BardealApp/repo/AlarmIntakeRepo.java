package org.eamcod.BardealApp.repo;

import org.eamcod.BardealApp.model.AlarmIntake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmIntakeRepo extends JpaRepository<AlarmIntake, Long> {

}
