package com.example.emailevents.service;

import com.example.emailevents.exception.NoSuchElementFoundException;
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

/**
 * Event service implementation. Aside from overriding the
 * interface method, this class will hold private methods
 * which will validate the information given from the
 * controller. The service will validate each given information,
 * if such filters are to be erroneous, the service will return
 * null. If the service received an empty list for the GET request,
 * it will throw an exception that the events were not found.
 */
@Service
public class EventServiceImpl implements EventService {

    private final EventRepo eventRepo;

    /**
     * Constructor autowiring
     *
     * @param eventRepo interface
     */
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

        event.setTimestamp(LocalDateTime.now());

        if (validateFields(event))
            return eventRepo.save(event);
        else
            throw new RuntimeException("cannot validate fields");
    }

    /**
     * Get event based on filters
     *
     * @param action    click or open
     * @param recipient client email
     * @param timestamp date and time of event
     * @return List of Events based on filters passed.
     */
    @Override
    public List<Event> getEvents(String action, String recipient, String timestamp) {

        Event event = new Event();
        event.setAction(action);
        event.setRecipient(recipient);

        if (!StringUtils.isBlank(timestamp)) {
            String updatedTimestampString = timestamp.replace('T', ' ');
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS");
            //TODO: handle parsing exception
            event.setTimestamp(LocalDateTime.parse(updatedTimestampString, formatter));
        }

        if (validateFields(event)) {
            List<Event> eventList = eventRepo.findAll(Example.of(event));
            if (eventList.isEmpty())
                throw new NoSuchElementFoundException("No elements found");
            else
                return eventList;
        }

        else
            throw new NoSuchElementFoundException("No elements found");
    }

    /**
     * Summarize all events to count clicks and open
     *
     * @param eventList filtered events
     * @return Summary of events click and open
     */
    @Override
    public Summary getEventSummary(List<Event> eventList) {

        return countClickAndOpen(eventList);
    }

    /**
     * Get event summary based on filter
     *
     * @param recipient  client email
     * @return Summary of events click and open
     */
    @Override
    public Summary getEventSummaryByRecipient(String recipient) {

        if (StringUtils.isNotBlank(recipient)) {
            if (!emailPatternMatch(recipient)) {
                throw new RuntimeException("invalid email");
            }
        }

        Event event = new Event();
        event.setRecipient(recipient);
        List<Event> eventList = eventRepo.findAll(Example.of(event));

        return countClickAndOpen(eventList);
    }

    /**
     * Get event summary based on filter
     *
     * @param timestamp start date
     * @param timestamp2 end date
     * @return Summary of events click and open
     */
    @Override
    public Summary getEventSummaryByTimestamps(String timestamp, String timestamp2) {

        LocalDateTime localDateTime;
        LocalDateTime localDateTime2;

        if (StringUtils.isNotBlank(timestamp) && StringUtils.isNotBlank(timestamp2)) {
            localDateTime = convertStringToLdt(timestamp);
            localDateTime2 = convertStringToLdt(timestamp2);
        } else {
            throw new RuntimeException("add start date and/or end date");
        }
        List<Event> eventList = eventRepo.findAllByTimestampBetween(localDateTime, localDateTime2);

        return countClickAndOpen(eventList);
    }

    /**
     * Method to verify is given email is valid with an @ symbol.
     *
     * @param email of event
     * @return true or false
     */
    private boolean emailPatternMatch(String email) {

        return Pattern.compile(Constants.EVENT_EMAIL_PATTERN).matcher(email).matches();
    }

    /**
     * Validate if action and recipient are correctly entered
     *
     * @param event obj
     * @return true or false
     */
    private boolean validateFields(Event event) {

        String action = event.getAction();
        if (StringUtils.isNotBlank(action)) {
            if (!action.equals(Constants.EVENT_CLICK) & !action.equals(Constants.EVENT_OPEN)) {
                throw new RuntimeException("action must be click or open");
            }
        }

        String recipient = event.getRecipient();
        if (StringUtils.isNotBlank(recipient)) {
            if (emailPatternMatch(event.getRecipient())) {
                throw new RuntimeException("invalid email");
            }
        }

        return true;
    }

    /**
     * Count click and open based on list of events passed
     *
     * @param eventList list
     * @return Summary of Events
     */
    private Summary countClickAndOpen(List<Event> eventList) {

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
     * Converter to turn a String to a LocalDateTime obj.
     *
     * @param timestamp LocalDateTime obj
     * @return Parsed LocalDateTime obj
     */
    private LocalDateTime convertStringToLdt(String timestamp) {

        String updatedTimestampString = timestamp.replace('T', ' ');
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS");
        return LocalDateTime.parse(updatedTimestampString, formatter);
    }
}
