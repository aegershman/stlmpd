package com.aegershman.stlmpd.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeSinceField {
    FIFTEEN_MIN("30m"),
    TWO_HOURS("2hr"),
    ANY("any");

    private final String timeSince;
}
