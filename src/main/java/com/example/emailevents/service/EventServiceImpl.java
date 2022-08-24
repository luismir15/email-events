package com.example.emailevents.service;

import com.example.emailevents.model.Event;
import com.example.emailevents.repo.EventRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepo eventRepo;

    @Autowired
    public EventServiceImpl(EventRepo eventRepo) {
        this.eventRepo = eventRepo;
    }

    /**
     * Insert new events
     *
     * @param event Event
     */
    @Override
    public Event insertEvent(Event event) {

        return eventRepo.save(event);
    }

    /**
     * Get events by action type
     *
     * @param action type of click or open
     * @return list of Event
     */
    @Override
    public List<Event> getEventsByAction(String action) {
        return null;
    }

    /**
     * Get events by recipient email address
     *
     * @param recipient email address
     * @return list of Event
     */
    @Override
    public List<Event> getEventsByRecipient(String recipient) {
        return null;
    }

    /**
     * Get events by local date/time
     *
     * @param timestamp date and time of event
     * @return List of Event
     */
    @Override
    public List<Event> getEventsByTimestamp(LocalDateTime timestamp) {
        return null;
    }
}
