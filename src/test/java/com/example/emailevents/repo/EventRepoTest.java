package com.example.emailevents.repo;

import com.example.emailevents.model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EventRepoTest {

    @Autowired
    private EventRepo eventRepo;

    @Test
    void whenFindAllByTimestampBetween_ThenReturnEvents() {
        List<Event> result = eventRepo.findAllByTimestampBetween(
                LocalDateTime.of(2021, 1,11, 13, 57, 35, 780),
                LocalDateTime.of(2021, 3,11, 13, 57, 35, 700)
        );

        assertEquals(2, result.size());
        assertTrue(result.stream()
                .map(Event::getId)
                .allMatch(id -> Arrays.asList(result.get(0).getId(), result.get(1).getId()).contains(id)));
    }

    @Test
    void findAllWithBeforeTimestamp() {

        List<Event> result =  eventRepo.findAllWithBeforeTimestamp(
                LocalDateTime.of(2021, 3,11, 13, 57, 35, 700)
        );

        System.out.println(Arrays.toString(result.toArray()));

        assertEquals(2, result.size());
        assertTrue(result.stream()
                .map(Event::getId)
                .allMatch(id -> Arrays.asList(result.get(0).getId(), result.get(1).getId()).contains(id)));
    }
}
