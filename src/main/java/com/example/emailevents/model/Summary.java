package com.example.emailevents.model;

import lombok.*;

/**
 * The Summary model will be given back to the client for
 * the GET /summary request. It will the counts of open
 * and click.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Summary {

    private Open open;
    private Click click;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    public static class Open {
        private long count;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    public static class Click {
        private long count;
    }
}
