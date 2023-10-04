package com.aegershman.stlmpd.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortField {
    ID("id"),
    CALL_TIME("callTime");

    private final String databaseFieldName;
}
