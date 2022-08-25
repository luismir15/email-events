package com.example.emailevents.service;

import com.example.emailevents.model.Event;
import com.example.emailevents.repo.EventRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
     * Find event based on Model passed
     *
     * @param event obj
     * @return List of Event
     */
    @Override
    public List<Event> getEvents(Event event) {
        return eventRepo.findAll(Example.of(event));
    }

    /**
     * Get all events
     *
     * @return List of Events
     */
    @Override
    public List<Event> getAllEvents() {

        return eventRepo.findAll();
    }
}
