package com.example.emailevents.repo;

import com.example.emailevents.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository to get information from Database.
 */
@Repository
public interface EventRepo extends JpaRepository<Event, UUID> {

}
