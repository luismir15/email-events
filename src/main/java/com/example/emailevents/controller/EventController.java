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
import java.util.Objects;

@RestController
@RequestMapping("api/email")
public class EventController {

    private final EventService eventService;

    private List<Event> eventList;

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
    public ResponseEntity<Message> getEvents(
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String recipient,
            @RequestParam(required = false) String timestamp
    ) {

        List<Event> filteredEventList = eventService.getEvents(action, recipient, timestamp);

        if (filteredEventList == null) {
            return new ResponseEntity<>(
                    new Message("please review query params input"),
                    HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity<>(new Message(filteredEventList), HttpStatus.OK);
    }

   //@GetMapping(value = "/summary")
   public ResponseEntity<Message> getEventSummary(
           @RequestParam(required = false) String action,
           @RequestParam(required = false) String recipient,
           @RequestParam(required = false) String timestamp
   ) {
        List<Event> eventListSummary = Objects.requireNonNull(getEvents(action, recipient, timestamp).getBody()).getEventList();
        if (eventListSummary == null || eventListSummary.isEmpty()) {
            return new ResponseEntity<>(
                    new Message("please review query params"),
                    HttpStatus.BAD_REQUEST
            );
        }

        Summary summary = eventService.getEventSummary(eventListSummary);

        return new ResponseEntity<>(
                new Message(summary),
                HttpStatus.OK
        );
   }

    @GetMapping(value = "/summary")
    public ResponseEntity<Message> getEventSummaryByRecipient(
            @RequestParam(required = false) String recipient,
            @RequestParam(required = false) String timestamp,
            @RequestParam(required = false) String timestamp2
    ) {

        if (StringUtils.isNotBlank(recipient) && (StringUtils.isBlank(timestamp) && StringUtils.isBlank(timestamp2))) {
            Summary recipientSummary = eventService.getEventSummaryByRecipient(recipient);
            return new ResponseEntity<>(new Message(recipientSummary), HttpStatus.OK);
        }
        else if ((StringUtils.isNotBlank(timestamp) && StringUtils.isNotBlank(timestamp2)) && StringUtils.isBlank(recipient) ) {
            Summary timestampSummary = eventService.getEventSummaryByTimestamps(timestamp, timestamp2);
            return new ResponseEntity<>(new Message(timestampSummary), HttpStatus.OK);
        } else {

            return new ResponseEntity<>(new Message(), HttpStatus.BAD_REQUEST);
        }
    }
}
