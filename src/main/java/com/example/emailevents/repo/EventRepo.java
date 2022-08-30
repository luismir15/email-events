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
 * Repository to get Events from Database. This interface will
 * be extending the JpaRepository API to fetch events with id
 * of type UUID.
 */
@Repository
public interface EventRepo extends JpaRepository<Event, UUID> {

    /**
     * Get all events between given timestamps
     * @param timestamp exclusive
     * @param timestamp2 inclusive
     * @return List of Events
     */
    List<Event> findAllByTimestampBetween(LocalDateTime timestamp, LocalDateTime timestamp2);

    /**
     * Query all event before the given timestamps
     * @param timestamp inclusive
     * @return List of Events
     */
    @Query("select e from Event e where e.timestamp <= :timestamp")
    List<Event> findAllWithBeforeTimestamp(@Param("timestamp") LocalDateTime timestamp);
}
