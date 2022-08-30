package com.example.emailevents.controller;

import com.example.emailevents.exception.NoSuchElementFoundException;
import com.example.emailevents.model.Event;
import com.example.emailevents.model.Message;
import com.example.emailevents.model.Summary;
import com.example.emailevents.service.EventService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Objects;

/**
 * REST Controller to process incoming Event requests. This
 * Controller will POST an event, GET events and GET summary of
 * events. There will be ExceptionHandlers which Spring will
 * recognize if an exception occurred. If such error were to happen
 * a ResponseEntity will be returned with a Message class which will
 * notify the client of the issue.
 */
@RestController
@RequestMapping("api/email")
public class EventController {

    public static final String TRACE = "trace";

    @Value("${reflectoring.trace:false}")
    private boolean printStackTrace;

    private final EventService eventService;

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

    /**
     * Add incoming events
     *
     * @param event obj from client request.
     * @return response entity
     */
    @PostMapping(value = "/events/v2")
    public Event addEventV2(@RequestBody @Validated Event event) {
        return eventService.insertEvent(event);
    }

    /**
     * GET request to retrieve a List of events based on filter params
     * @param action click or open
     * @param recipient client email
     * @param timestamp time and date of event
     * @return List of Events
     */
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

    /**
     * GET request to retrieve a List of events based on filter params
     * @param action click or open
     * @param recipient client email
     * @param timestamp time and date of event
     * @return List of Events
     */
    @GetMapping(value = "/events/v2")
    public List<Event> getEventsV2(
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String recipient,
            @RequestParam(required = false) String timestamp
    ) {
        return eventService.getEvents(action, recipient, timestamp);
    }

   //@GetMapping(value = "/summary")
//   public ResponseEntity<Message> getEventSummary(
//           @RequestParam(required = false) String action,
//           @RequestParam(required = false) String recipient,
//           @RequestParam(required = false) String timestamp
//   ) {
//        List<Event> eventListSummary = Objects.requireNonNull(getEvents(action, recipient, timestamp).getBody()).getEventList();
//        if (eventListSummary == null || eventListSummary.isEmpty()) {
//            return new ResponseEntity<>(
//                    new Message("please review query params"),
//                    HttpStatus.BAD_REQUEST
//            );
//        }
//
//        Summary summary = eventService.getEventSummary(eventListSummary);
//
//        return new ResponseEntity<>(
//                new Message(summary),
//                HttpStatus.OK
//        );
//   }

    /**
     * GET request to summarize click and open of returned list of events
     *
     * @param recipient client email
     * @param timestamp start date
     * @param timestamp2 end date
     * @return Summary of Events
     */
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

    /**
     * GET request to summarize click and open of returned list of events
     *
     * @param recipient client email
     * @param timestamp start date
     * @param timestamp2 end date
     * @return Summary of Events
     */
    @GetMapping(value = "/summary/v2")
    public Summary getEventSummary(
            @RequestParam(required = false) String recipient,
            @RequestParam(required = false) String timestamp,
            @RequestParam(required = false) String timestamp2
    ) {

        if (StringUtils.isNotBlank(recipient) && (StringUtils.isBlank(timestamp) && StringUtils.isBlank(timestamp2))) {
            return eventService.getEventSummaryByRecipient(recipient);
        }
        else if ((StringUtils.isNotBlank(timestamp) && StringUtils.isNotBlank(timestamp2)) && StringUtils.isBlank(recipient) ) {
            return eventService.getEventSummaryByTimestamps(timestamp, timestamp2);
        } else {
            throw new NoSuchElementFoundException("could calculate summary. please review if email or dates are valid" +
                    "also don't combine recipient with dates. use filters separately");
        }
    }

    /**
     * Custom exception handler that will throw a NoSuchElementFoundException
     * if the Repo could to locate events.
     *
     * @param exception NoSuchElementFoundException
     * @param request GET /events and /summary
     * @return ResponseEntity with exception message and status code.
     */
    @ExceptionHandler(NoSuchElementFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Message> handleItemNotFoundException(
            NoSuchElementFoundException exception,
            WebRequest request
    ) {
        return buildErrorResponse(exception, request);
    }

    /**
     * Exception handler that will throw a MethodArgumentNotValidException
     * for invalid arguments passed in the request.
     *
     * @param ex MethodArgumentNotValidException
     * @return ResponseEntity with exception messages and status code
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Message> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex
    ) {
        Message errorResponse = new Message(
                "Validation error. Check 'errors' field for details.",
                HttpStatus.UNPROCESSABLE_ENTITY.value()
        );

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(),
                    fieldError.getDefaultMessage());
        }
        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    /**
     * Exception handler that will throw any other uncaught exception.
     *
     * @param exception general Exception
     * @param request all request
     * @return ResponseEntity with unknown exception message and status code
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Message> handleAllUncaughtException(
            Exception exception,
            WebRequest request){

        return buildErrorResponse(
                exception,
                request
        );
    }


    /**
     * This method will build the error message that will be
     * returned to the client.
     *
     * @param exception Exception
     * @param request   all request
     * @return Build Message in ResponseEntity
     */
    private ResponseEntity<Message> buildErrorResponse(
            Exception exception,
            WebRequest request
    ) {
        return buildErrorResponse(
                exception,
                HttpStatus.NOT_FOUND,
                request);
    }

    /**
     * This method will build the error message that will be
     * returned to the client.
     *
     * @param exception Exception
     * @param httpStatus HttpStatus
     * @param request all request
     * @return Build Message in ResponseEntity
     */
    private ResponseEntity<Message> buildErrorResponse(
            Exception exception,
            HttpStatus httpStatus,
            WebRequest request
    ) {
        Message errorResponse = new Message(
                exception.getMessage() ,
                httpStatus.value()
        );

        if(printStackTrace && isTraceOn(request)){
            errorResponse.setStackTrace(ExceptionUtils.getStackTrace(exception));
        }
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    /**
     * Boolean method verify is the stack trace is triggered.
     *
     * @param request WebRequest
     * @return true or false
     */
    private boolean isTraceOn(WebRequest request) {
        String [] value = request.getParameterValues(TRACE);
        return Objects.nonNull(value)
                && value.length > 0
                && value[0].contentEquals("true");
    }
}
