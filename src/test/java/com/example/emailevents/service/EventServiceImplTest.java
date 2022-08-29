package com.example.emailevents.service;

import com.example.emailevents.model.Event;
import com.example.emailevents.model.Summary;
import com.example.emailevents.repo.EventRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Service implementation test cases. The main goal is to mock the repo.
 * Mockito will also verify that the repo performed the correct action.
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepo eventRepo;

    private EventService eventService;

    /**
     * Before each test we will be setting up the event
     * service by injecting a mocked repo.
     */
    @BeforeEach
    void setUp() {

        eventService = new EventServiceImpl(eventRepo);
    }

    /**
     * Test if given an Event it will return that same Event when inserting.
     */
    @Test
    void givenEvent_whenInsertEvent_ThenReturnEventObject() {

        Event event = new Event(
                UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
                "click",
                "Subscribe Now",
                "eric@piloto151.com",
                LocalDateTime.of(2021, 1,11, 13, 57, 35, 780),
                null
        );

        Mockito.when(eventRepo.save(event)).thenReturn(event);

        Event actualEvent = eventService.insertEvent(event);

        Mockito.verify(eventRepo).save(event);

        assertEquals(event, actualEvent);
    }

    /**
     * Test if given action, recipient and timestamp filters it will return
     * a filtered list when getting that event or events.
     */
    @Test
    void givenSetOfFilters_whenGetEvents_thenReturnListOfEvents() {

        String action = "click";
        String recipient = "eric@piloto151.com";
        String timestamp = "2021-02-11T08:57:35.78";

        Event event = new Event();
        event.setAction(action);
        event.setRecipient(recipient);
        String updatedTimestampString = timestamp.replace('T', ' ');
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS");
        event.setTimestamp(LocalDateTime.parse(updatedTimestampString, formatter));

        Mockito.when(eventRepo.findAll(Example.of(event))).thenReturn(new ArrayList<>());

        List<Event> actualEvents = eventService.getEvents(action, recipient, timestamp);

        Mockito.verify(eventRepo).findAll(Example.of(event));

        assertThat(actualEvents).isNotNull();
    }

    /**
     * If given a list of events, it will return a summary of click and open.
     */
    @Test
    void givenListOfEvent_whenGetEventSummary_thenReturnSummaryOfEvents() {

        String action = "click";
        String recipient = "eric@piloto151.com";
        String timestamp = "2021-02-11T08:57:35.78";

        Event event = new Event();
        event.setAction(action);
        event.setRecipient(recipient);
        String updatedTimestampString = timestamp.replace('T', ' ');
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS");
        event.setTimestamp(LocalDateTime.parse(updatedTimestampString, formatter));

        Mockito.when(eventRepo.findAll(Example.of(event))).thenReturn(new ArrayList<>());

        List<Event> eventList = eventService.getEvents("click", "eric@piloto151.com", "2021-02-11T08:57:35.78");

        Summary summary = eventService.getEventSummary(eventList);

        assertThat(summary).isNotNull();
    }
}
