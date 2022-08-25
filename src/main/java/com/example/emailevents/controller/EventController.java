package com.example.emailevents.controller;

import com.example.emailevents.model.Event;
import com.example.emailevents.repo.EventRepo;
import com.example.emailevents.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/email")
public class EventController {

    private final EventService eventService;

    @Autowired
    private EventRepo eventRepo;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping(value = "/events")
    public Event addEvent(@RequestBody Event event) {

        event.setTimestamp(LocalDateTime.now());

        return eventService.insertEvent(event);
    }

    @GetMapping(value = "/events")
    public List<Event> getAllEvents() {

        return eventRepo.findAll();
    }

}
