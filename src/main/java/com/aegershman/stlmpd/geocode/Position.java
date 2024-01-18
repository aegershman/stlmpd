package com.aegershman.stlmpd.geocode;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Position {
    @JsonProperty("lat")
    public String latitude;
    @JsonProperty("lng")
    public String longitude;
}
