package com.hellofresh.datastats.service;

import com.hellofresh.datastats.model.DataEvent;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class PayloadService {
    public List<DataEvent> processPayload(String payload) {
        List<DataEvent> events = new ArrayList<>();
        String[] lines = payload.split("\n");
        for (String line : lines) {
            String[] data = line.split(",");
            // TODO: Here we should check for errors in payload and only add to the result the valid events
            //  For the invalid events we can notify the error (e.g  x has more than 10 digits, y is out of range, timestamp is not valid)
            DataEvent event = new DataEvent(new Timestamp(Long.parseLong(data[0])), Double.parseDouble(data[1]), Integer.parseInt(data[2]));
            events.add(event);
        }
        return events;

    }
}
