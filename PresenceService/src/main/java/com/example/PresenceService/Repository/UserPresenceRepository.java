package com.example.PresenceService.Repository;

import com.example.PresenceService.Entity.UserPresence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@Repository
public interface UserPresenceRepository extends JpaRepository<UserPresence, Long> {

    Optional<UserPresence> findByUserId(Long aLong);

}
