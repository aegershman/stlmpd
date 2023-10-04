package com.aegershman.stlmpd.geocoding;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Position {
    @JsonProperty("lat")
    public String latitude;
    @JsonProperty("lng")
    public String longitude;
}
