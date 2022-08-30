package com.example.emailevents.service;

import com.example.emailevents.model.Event;
import com.example.emailevents.model.Summary;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    /**
     * Insert new events
     * @param event Event
     */
     Event insertEvent(Event event);

    /**
     * Get event based on filters
     * @param action click or open
     * @param recipient client email
     * @param timestamp date and time of event
     * @return
     */
     List<Event> getEvents(String action, String recipient, String timestamp);

    /**
     * Summarize all events to count clicks and open
     * @param eventList filtered events
     * @return Summary of events click and open
     */
     Summary getEventSummary(List<Event> eventList);

    /**
     * Get event summary based on filter
     * @param recipient client email
     * @return Summary of events click and open
     */
     Summary getEventSummaryByRecipient(String recipient);

    /**
     * Get event summary based on filter
     * @param timestamp
     * @param timestamp2
     * @return Summary of events click and open
     */
    Summary getEventSummaryByTimestamps(String timestamp, String timestamp2);
}
