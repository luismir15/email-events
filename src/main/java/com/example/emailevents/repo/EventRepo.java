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

    /**
     * Find existing events by action type
     * @param action type of click or open
     * @return list of Event
     */
    public List<Event> findEventsByAction(String action);

    /**
     * Find existing events by recipient email address
     * @param recipient email address
     * @return list of Event
     */
    public List<Event> findEventsByRecipient(String recipient);

    /**
     * Find existing events by local date/time
     * @param timestamp date and time of event
     * @return List of Event
     */
    public List<Event> findEventsByTimestamp(LocalDateTime timestamp);
}
