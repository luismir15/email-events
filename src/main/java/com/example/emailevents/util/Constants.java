package com.example.emailevents.util;

/**
 * The Constants interface will mainly be used to validate fields.
 * event click and open constant will limit the action and the
 * email pattern is to verify is the recipient has the correct
 * syntax.
 */
public interface Constants {

    String EVENT_CLICK = "click";
    String EVENT_OPEN = "open";
    String EVENT_EMAIL_PATTERN = "^(.+)@(\\S+)$";
}
