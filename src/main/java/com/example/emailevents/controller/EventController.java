package com.example.emailevents.controller;

import com.example.emailevents.model.Event;
import com.example.emailevents.model.Summary;
import com.example.emailevents.service.EventService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("api/email")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * TODO: add response entity
     * @param event
     * @return
     */
    @PostMapping(value = "/events")
    public Event addEvent(@RequestBody Event event) {

        event.setTimestamp(LocalDateTime.now());

        return eventService.insertEvent(event);
    }

    @GetMapping(value = "/events")
    public List<Event> getEvents(
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String recipient,
            @RequestParam(required = false) String timestamp
    ) {

        Event event = new Event();
        event.setAction(action);
        event.setRecipient(recipient);

        if (!StringUtils.isBlank(timestamp)) {
            String updatedTimestampString = timestamp.replace('T', ' ');
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS");
            event.setTimestamp(LocalDateTime.parse(updatedTimestampString, formatter));
        }

        return eventService.getEvents(event);
    }

   @GetMapping(value = "/summary")
   public Summary getEventSummary(
           @RequestParam(required = false) String action,
           @RequestParam(required = false) String recipient,
           @RequestParam(required = false) String timestamp
   ) {
        List<Event> eventListSummary = getEvents(action, recipient, timestamp);
        if (eventListSummary.isEmpty()) {
            return null;
        }

        return eventService.getEventSummary(eventListSummary);
   }

    @GetMapping(value = "/events/all")
    public List<Event> getAllEvents() {

        return eventService.getAllEvents();
    }

}
