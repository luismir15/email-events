package com.example.emailevents.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {

    private String message;

    private Event event;

    private List<Event> eventList;

    private Summary summary;

    public Message(String message) {
        this.message = message;
    }

    public Message(String message, Event event) {
        this.message = message;
        this.event = event;
    }

    public Message(List<Event> eventList) {
        this.eventList = eventList;
    }

    public Message(Summary summary) {
        this.summary = summary;
    }
}
