package com.example.emailevents.service;

import com.example.emailevents.model.Event;
import com.example.emailevents.model.Summary;
import com.example.emailevents.repo.EventRepo;
import com.example.emailevents.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

        if (validateFields(event))
            return eventRepo.save(event);
        else
            return null;
    }

    /**
     * Get event based on filters
     *
     * @param action    click or open
     * @param recipient client email
     * @param timestamp date and time of event
     * @return
     */
    @Override
    public List<Event> getEvents(String action, String recipient, String timestamp) {

        Event event = new Event();
        event.setAction(action);
        event.setRecipient(recipient);

        if (!StringUtils.isBlank(timestamp)) {
            String updatedTimestampString = timestamp.replace('T', ' ');
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS");
            event.setTimestamp(LocalDateTime.parse(updatedTimestampString, formatter));
        }

        if (validateFields(event))
            return eventRepo.findAll(Example.of(event));
        else
            return null;
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
