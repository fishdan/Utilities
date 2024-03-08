package com.fishdan;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;


import java.util.Optional;

public class IcsToGoogleCalendar {

    public static URL parseIcsFile(String filePath) {
        try {
            FileInputStream fin = new FileInputStream(filePath);
            CalendarBuilder builder = new CalendarBuilder();
            Calendar calendar = builder.build(fin);

            for (Component event : calendar.getComponents(Component.VEVENT)) {
                if (event instanceof VEvent) {
                    VEvent vEvent = (VEvent) event;

                    Optional<DtStart> startOpt = vEvent.getProperty(Property.DTSTART);
                    DtStart start = startOpt.orElseThrow(() -> new RuntimeException("Start date is missing"));

                    Optional<DtEnd> endOpt = vEvent.getProperty(Property.DTEND);
                    DtEnd end = endOpt.orElseThrow(() -> new RuntimeException("End date is missing"));

                    Optional<Summary> summaryOpt = vEvent.getProperty(Property.SUMMARY);
                    Summary summary = summaryOpt.orElseThrow(() -> new RuntimeException("Summary is missing"));

                    Optional<Description> descriptionOpt = vEvent.getProperty(Property.DESCRIPTION);
                    Description description = descriptionOpt.orElseThrow(() -> new RuntimeException("Description is missing"));

                    Optional<Location> locationOpt = vEvent.getProperty(Property.LOCATION);
                    Location location = locationOpt.orElseThrow(() -> new RuntimeException("Location is missing"));

                    // Now you have the event details, you can create a URL for Google Calendar
                    return createGoogleCalendarUrl(start, end, summary, description, location); // Open this URL in a browser
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static URL createGoogleCalendarUrl(DtStart start, DtEnd end, Summary summary, Description description, Location location) {
        try {
            String baseUrl = "https://calendar.google.com/calendar/r/eventedit?";
            String text = "text=" + URLEncoder.encode(summary.getValue(), StandardCharsets.UTF_8.name());
            String details = "&details=" + URLEncoder.encode(description.getValue(), StandardCharsets.UTF_8.name());
            String locationString = "&location=" + URLEncoder.encode(location.getValue(), StandardCharsets.UTF_8.name());

            // Assuming start and end dates are formatted correctly for Google Calendar
            // You might need to format these values to match Google Calendar's expected format
            String startFormatted = formatDateTimeForGoogleCalendar(start.getDate());
            String endFormatted = formatDateTimeForGoogleCalendar((end.getDate()));
            String dates = "&dates=" + URLEncoder.encode(startFormatted, StandardCharsets.UTF_8.name())
                    + "/" + URLEncoder.encode(endFormatted, StandardCharsets.UTF_8.name());

            String fullUrl = baseUrl + text + details + locationString + dates;
            return new URL(fullUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Or handle the error more gracefully depending on your application's requirements
        }
    }


    private static String formatDateTimeForGoogleCalendar(Temporal temporal) {
        // Ensure the temporal value is in the desired timezone. Convert to ZonedDateTime as necessary.
        ZonedDateTime zdt = ZonedDateTime.ofInstant(DateTimeUtils.toInstant(temporal), ZoneId.of("UTC")); // Example uses UTC

        // Format to the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'").withZone(ZoneId.of("UTC"));
        return formatter.format(zdt);
    }



    public static void main(String[] args) {
        // Replace "path/to/your/event.ics" with the actual path to an ICS file to test your method
        try {
            Desktop.getDesktop().browse(parseIcsFile(args[0]).toURI());
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}

