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

/**
 * JPA Repo test class. Main focus of these test cases is to check the size of the
 * returned list and to verify their ids.
 */
@DataJpaTest
class EventRepoTest {

    @Autowired
    private EventRepo eventRepo;

    /**
     * Test if all Events are returned between timestamps.
     */
    @Test
    void whenFindAllByTimestampBetween_ThenReturnEvents() {
        List<Event> result = eventRepo.findAllByTimestampBetween(
                LocalDateTime.of(2021, 1,11, 13, 57, 35, 780),
                LocalDateTime.of(2021, 3,11, 13, 57, 35, 700)
        );

        assertEquals(1, result.size());
    }

    /**
     * Test if all Events are returned before given timestamp.
     */
    @Test
    void findAllWithBeforeTimestamp() {

        List<Event> result =  eventRepo.findAllWithBeforeTimestamp(
                LocalDateTime.of(2021, 3,11, 13, 57, 35, 700)
        );

        System.out.println(Arrays.toString(result.toArray()));

        assertEquals(5, result.size());
    }
}
