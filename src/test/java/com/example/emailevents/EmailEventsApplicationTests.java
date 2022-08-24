package com.example.emailevents;

import com.example.emailevents.model.Event;
import com.example.emailevents.repo.EventRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class EmailEventsApplicationTests {

    @Autowired
    private EventRepo eventRepo;

    @Test
    public void whenFindEventByAction_ThenReturnListOfEvents() {

        List<Event> event = eventRepo.findEventsByAction("click");

        assertNotNull(event);
        assertTrue(event.stream()
                .map(Event::getAction)
                .allMatch(action -> Objects.equals(action, "click") || Objects.equals(action, "open")));
    }

    @Test
    public void whenFindEventByRecipient_ThenReturnListOfEvents() {

        List<Event> event = eventRepo.findEventsByRecipient("eric@piloto151.com");

        assertNotNull(event);
        assertTrue(event.stream()
                .map(Event::getRecipient)
                .allMatch(action -> Objects.equals(action, "eric@piloto151.com")));
    }

    @Test
    public void whenFindEventByTimestamp_ThenReturnListOfEvents() {

        List<Event> event = eventRepo.findEventsByTimestamp(
                LocalDateTime.of(2021, 2,11, 13, 57, 35, 700)
        );

        assertNotNull(event);
    }
}
