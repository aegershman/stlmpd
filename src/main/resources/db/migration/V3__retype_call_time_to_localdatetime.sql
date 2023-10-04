ALTER TABLE service_call
  ALTER COLUMN call_time
    TYPE timestamp without time zone
        USING call_time::timestamp without time zone
