package com.example.emailevents.service;

import com.example.emailevents.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    /**
     * Insert new events
     * @param event Event
     */
    public Event insertEvent(Event event);

    /**
     * Get events by action type
     * @param action type of click or open
     * @return list of Event
     */
    public List<Event> getEventsByAction(String action);

    /**
     * Get events by recipient email address
     * @param recipient email address
     * @return list of Event
     */
    public List<Event> getEventsByRecipient(String recipient);

    /**
     * Get events by local date/time
     * @param timestamp date and time of event
     * @return List of Event
     */
    public List<Event> getEventsByTimestamp(LocalDateTime timestamp);
}
