package com.example.emailevents.repo;

import com.example.emailevents.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository to get information from Database.
 */
@Repository
public interface EventRepo extends JpaRepository<Event, UUID> {

    List<Event> findAllByTimestampBetween(LocalDateTime timestamp, LocalDateTime timestamp2);

    @Query("select e from Event e where e.timestamp <= :timestamp")
    List<Event> findAllWithBeforeTimestamp(@Param("timestamp") LocalDateTime timestamp);
}
