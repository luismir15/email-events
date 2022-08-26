package com.example.emailevents.service;

import com.example.emailevents.model.Event;
import com.example.emailevents.model.Summary;
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

        if (validateFields(event))
            return eventRepo.findAll(Example.of(event));
        else
            return null;
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

    /**
     * @return
     */
    @Override
    public Summary getEventSummary(List<Event> eventList) {

        Event event = new Event();
        event.setSummary(new Summary());
        long totalClicks = eventList
                .stream()
                .filter(clicks -> clicks.getAction().equals(Constants.EVENT_CLICK))
                .count();
        long totalOpens = eventList
                .stream()
                .filter(open -> open.getAction().equals(Constants.EVENT_OPEN))
                .count();

        event.getSummary().setClick(new Summary.Click(totalClicks));
        event.getSummary().setOpen(new Summary.Open(totalOpens));

        return event.getSummary();
    }

    /**
     * Method to verify is given email is valid with an @ symbol.
     * @param email of event
     * @param pattern to match email
     * @return true or false
     */
    private boolean emailPatternMatch(String email, String pattern) {

        return Pattern.compile(pattern).matcher(email).matches();
    }

    /**
     * Validate if action and recipient are correctly entered
     * @param event obj
     * @return true or false
     */
    private boolean validateFields(Event event) {

        String action = event.getAction();
        if (StringUtils.isNotBlank(action)) {
            if (!action.equals(Constants.EVENT_CLICK) & !action.equals(Constants.EVENT_OPEN)) {
                return false;
            }
        }

        String recipient = event.getRecipient();
        if (StringUtils.isNotBlank(recipient))
            return emailPatternMatch(event.getRecipient(), Constants.EVENT_EMAIL_PATTERN);

        return true;
    }
}
