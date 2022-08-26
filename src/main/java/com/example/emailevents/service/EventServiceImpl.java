package com.example.emailevents.service;

import com.example.emailevents.model.Event;
import com.example.emailevents.repo.EventRepo;
import com.example.emailevents.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

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

        String action = event.getAction();
        if (StringUtils.isNotBlank(action)) {
            if (!action.equals(Constants.EVENT_CLICK) & !action.equals(Constants.EVENT_OPEN)) {
                return null;
            }
        }

        if (!emailPatternMatch(event.getRecipient(), Constants.EVENT_EMAIL_PATTERN)) {
            return null;
        }


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

    private boolean emailPatternMatch(String email, String pattern) {

        return Pattern.compile(pattern).matcher(email).matches();
    }
}