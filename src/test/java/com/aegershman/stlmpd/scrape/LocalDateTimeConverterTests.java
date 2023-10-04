package com.aegershman.stlmpd.scrape;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LocalDateTimeConverterTests {

    @Test
    void parseDateTime_doubleDigitHour() {
        String toParse = "2023-10-04 10:03:22";
        Assertions.assertDoesNotThrow(() ->
                LocalDateTimeConverter.convert(toParse)
        );
    }

    @Test
    void parseDateTime_singleDigitHour() {
        String toParse = "2023-10-04 9:03:22";
        Assertions.assertDoesNotThrow(() ->
                LocalDateTimeConverter.convert(toParse)
        );
    }

}
