package com.example.emailevents.controller;

import com.example.emailevents.model.Event;
import com.example.emailevents.model.Message;
import com.example.emailevents.model.Summary;
import com.example.emailevents.service.EventService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("api/email")
public class EventController {

    private final EventService eventService;

    /**
     * Constructor autowiring
     * @param eventService interface autowired
     */
    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Add incoming events
     *
     * @param event obj from client request.
     * @return response entity
     */
    @PostMapping(value = "/events")
    public ResponseEntity<Message> addEvent(@RequestBody(required = false) Event event) {

        if (event == null || StringUtils.isBlank(event.getAction()) || StringUtils.isBlank(event.getRecipient())) {
            return new ResponseEntity<>(
                    new Message("request body is null or make sure that you added action and recipient"),
                    HttpStatus.BAD_REQUEST
            );
        }

        event.setTimestamp(LocalDateTime.now());

        Event validatedEvent = eventService.insertEvent(event);

        if (validatedEvent == null) {
            return new ResponseEntity<>(
                    new Message("request body is null or make sure that you added action and recipient"),
                    HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity<>(
                new Message("successfully added event", event),
                HttpStatus.OK
        );
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
}
