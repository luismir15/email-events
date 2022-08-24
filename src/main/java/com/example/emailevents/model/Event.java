package com.example.emailevents.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Event entity to model email events
 */
@Entity
@Table(name = "event")
public class Event {

    /**
     * Event primary key ID of type Universally Unique Identifier.
     * Here Hibernate annotation are being used to avoid collision
     * when creating a random ID.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
    @Type(type = "uuid-char")
    @Column(name = "event_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "event_action", updatable = false, nullable = false)
    private String action;

    @Column(name = "event_subject", updatable = false, nullable = false)
    private String subject;

    @Column(name = "event_recipient", updatable = false, nullable = false)
    private String recipient;

    @Column(name = "event_timestamp", updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime timestamp;
}
