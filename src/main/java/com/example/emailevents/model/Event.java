package com.example.emailevents.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Event entity to model email events.
 * Fields:
 *  id: UUID => Event primary key ID of type Universally Unique Identifier.
 *              Here Hibernate annotation are being used to avoid collision when creating a random ID.
 *  action: String => Actions that represent an event limited to "click" and "open"
 *  subject: String => Subject title of a email.
 *  recipient: String => email address
 *  timestamp: LocalDateTime => time & data of event triggered
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "event")
public class Event {

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

    @Column(name = "event_subject", updatable = false, nullable = true)
    private String subject;

    @Column(name = "event_recipient", updatable = false, nullable = false)
    private String recipient;

    @Column(name = "event_timestamp", updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime timestamp;

    @Transient
    @JsonIgnore
    private Summary summary;
}
