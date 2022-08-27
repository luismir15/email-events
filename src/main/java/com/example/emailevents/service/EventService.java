package com.example.emailevents.service;

import com.example.emailevents.model.Event;
import com.example.emailevents.model.Summary;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    /**
     * Insert new events
     * @param event Event
     */
     Event insertEvent(Event event);

    /**
     * Find event based on Model passed
     * @param event obj
     * @return Lisr of Event
     */
     List<Event> getEvents(Event event);

     Summary getEventSummary(List<Event> eventList);
}
