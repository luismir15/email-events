package com.example.emailevents.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Message model to create custom message for POSTs and GETs request.
 * This model will also have a roll when creating custom error message
 * to return to the client.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {

    private String message;
    private int httpStatus;
    private String stackTrace;
    private List<ValidationError> validationErrorList;

    public Message(String message, int httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class ValidationError {
        private String field;
        private String message;
    }

    public void addValidationError(String field, String message) {
        if (Objects.isNull(validationErrorList)) {
            validationErrorList = new ArrayList<>();
        }

        validationErrorList.add(new ValidationError(field, message));
    }


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
