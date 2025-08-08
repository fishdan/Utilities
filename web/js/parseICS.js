

/**
 * Parses an ICS date-time string (e.g., 20250529T230000Z) into a JavaScript Date object.
 * Handles UTC 'Z' or implied UTC for standard ICS formats.
 * @param {string} icsDateTimeString The date-time string from ICS.
 * @returns {Date|null} A Date object if successful, otherwise null.
 */
function parseIcsDateTimeToDate(icsDateTimeString) {
    // Regex to match YYYYMMDDTHHMMSS or YYYYMMDDTHHMMSSZ
    const icsRegex = /(\d{4})(\d{2})(\d{2})T(\d{2})(\d{2})(\d{2})(Z)?/;
    const match = icsDateTimeString.match(icsRegex);

    if (match) {
        // Construct Date in UTC
        // Month is 0-indexed in JavaScript Date
        return new Date(Date.UTC(
            parseInt(match[1], 10), // Year
            parseInt(match[2], 10) - 1, // Month
            parseInt(match[3], 10), // Day
            parseInt(match[4], 10), // Hour
            parseInt(match[5], 10), // Minute
            parseInt(match[6], 10)  // Second
        ));
    }
    console.warn(`Could not parse ICS date-time string: ${icsDateTimeString}`);
    return null;
}

/**
 * Parses an ICS DURATION string (e.g., PT1H, P1DT12H30M) into milliseconds.
 * @param {string} durationString The DURATION string from ICS.
 * @returns {number} The duration in milliseconds. Returns 0 for invalid formats.
 */
function parseIcsDurationToMilliseconds(durationString) {
    let totalMilliseconds = 0;
    // Regex for duration: P[nD][T[nH][nM][nS]]
    const regex = /P(?:(\d+)D)?T?(?:(\d+)H)?(?:(\d+)M)?(?:(\d+)S)?/;
    const match = durationString.match(regex);

    if (match) {
        const days = parseInt(match[1] || '0', 10);
        const hours = parseInt(match[2] || '0', 10);
        const minutes = parseInt(match[3] || '0', 10);
        const seconds = parseInt(match[4] || '0', 10);

        totalMilliseconds += days * 24 * 60 * 60 * 1000;
        totalMilliseconds += hours * 60 * 60 * 1000;
        totalMilliseconds += minutes * 60 * 1000;
        totalMilliseconds += seconds * 1000;
    } else {
        console.warn(`Invalid DURATION format: ${durationString}`);
    }
    return totalMilliseconds;
}

/**
 * Formats a Date object or a date string into the YYYYMMDDTHHMMSSZ format for Google Calendar.
 * This is crucial for consistent output.
 * @param {Date|string} dtInput The date input (Date object or string).
 * @returns {string} The formatted date string, or empty string if invalid.
 */
/**
 * Formats a Date object or a date string into the YYYYMMDDTHHMMSSZ format for Google Calendar.
 * This is crucial for consistent output.
 * @param {Date|string} dtInput The date input (Date object or string).
 * @returns {string} The formatted date string, or empty string if invalid.
 */
function formatDateTime(dtInput) {
    let date;

    if (dtInput instanceof Date) {
        date = dtInput; // Already a Date object, use it directly
    } else if (typeof dtInput === 'string') {
        // Try parsing as a standard ISO string or other common formats first
        try {
            date = new Date(dtInput);
            // Check if it's a valid date after parsing
            if (isNaN(date.getTime())) {
                throw new Error("Invalid date string for new Date()");
            }
        } catch (e) {
            // If new Date() fails, try parsing as an ICS specific format
            date = parseIcsDateTimeToDate(dtInput);
            if (!date) { // If ICS parser also fails
                console.error(`Could not parse date string: ${dtInput}`, e);
                return '';
            }
        }
    } else {
        console.error(`Invalid date input type: ${typeof dtInput}`, dtInput);
        return '';
    }

    // Final check for validity before formatting
    if (isNaN(date.getTime())) {
        console.error(`Invalid date object after all parsing attempts: ${dtInput}`);
        return '';
    }

    // Google Calendar expects UTC time without the milliseconds part
    return date.toISOString().replace(/[-:]|\.\d{3}/g, '');
}

// Ensure the helper functions parseIcsDateTimeToDate, parseIcsDurationToMilliseconds,
// parseICSData, createGoogleCalendarLink, displayEvents, and parseICS
// are all present as provided in the previous comprehensive answer.

/**
 * Creates a Google Calendar "Add Event" link.
 * Handles events with DTEND or DURATION, or defaults to 1 hour if neither is present.
 * @param {object} event The event object containing summary, start, end (optional), description (optional), location (optional), duration (optional).
 * @returns {string} The URL for adding the event to Google Calendar.
 */
function createGoogleCalendarLink(event) {
    const baseUrl = 'https://calendar.google.com/calendar/r/eventedit';
    let params = `text=${encodeURIComponent(event.summary || 'Untitled Event')}`;

    let dtStart = event.start;
    let dtEnd = event.end; // This might be undefined or the direct DTEND from ICS

    // Prioritize DURATION if DTEND is not explicitly provided
    if (event.duration && dtStart && !dtEnd) {
        const startDate = parseIcsDateTimeToDate(dtStart);
        if (startDate) {
            const durationMilliseconds = parseIcsDurationToMilliseconds(event.duration);
            const endDate = new Date(startDate.getTime() + durationMilliseconds);
            dtEnd = endDate.toISOString(); // Convert to ISO string for consistent handling
        } else {
            console.warn(`DTSTART could not be parsed for event with duration: ${dtStart}`);
        }
    } else if (!dtEnd && dtStart) {
        // Fallback: If no DTEND and no DURATION, assume 1-hour event
        const startDate = parseIcsDateTimeToDate(dtStart); // Ensure we parse it to a Date object first
        if (startDate) {
            startDate.setHours(startDate.getHours() + 1);
            dtEnd = startDate.toISOString(); // Convert to ISO string for consistent handling
        } else {
            console.warn(`DTSTART could not be parsed for event, defaulting to 1 hour: ${dtStart}`);
        }
    }

    // Now format both start and end times
    const formattedStart = formatDateTime(dtStart);
    const formattedEnd = formatDateTime(dtEnd);

    // Ensure dates are valid before adding to params
    if (formattedStart && formattedEnd) {
        params += `&dates=${formattedStart}/${formattedEnd}`;
    } else {
        console.error(`Missing or invalid start/end dates for event: ${event.summary}`);
        // Consider handling this error, maybe don't return a link or return an error message
        return '#'; // Or throw an error
    }


    if (event.description) {
        params += `&details=${encodeURIComponent(event.description)}`;
    }
    if (event.location) {
        params += `&location=${encodeURIComponent(event.location)}`;
    }
    if (event.uid) { // Include UID for better event management (optional, but good practice)
        params += `&eid=${encodeURIComponent(event.uid)}`;
    }

    return `${baseUrl}?${params}`;
}


// --- Main ICS Parsing and Display Logic ---

/**
 * Handles the file input and initiates ICS parsing.
 */
function parseICS() {
    const fileInput = document.getElementById('fileInput');
    if (fileInput.files.length > 0) {
        const reader = new FileReader();
        reader.onload = function(e) {
            const contents = e.target.result;
            const events = parseICSData(contents);
            displayEvents(events);
        };
        reader.readAsText(fileInput.files[0]);
    } else {
        alert('Please select an ICS file.');
    }
}

/**
 * Parses the raw ICS data string into an array of event objects.
 * This parser is basic and may not handle all complex ICS features (e.g., recurring events, complex timezones).
 * @param {string} icsData The raw content of the ICS file.
 * @returns {Array<object>} An array of event objects.
 */
function parseICSData(icsData) {
    const events = [];
    // Split by newlines, handling different OS line endings, and filter out empty lines
    let lines = icsData.split(/\r\n|\n|\r/).map(line => line.trim()).filter(line => line !== '');
    let event = null;

    // A simple state machine to parse VEVENT blocks
    lines.forEach(line => {
        if (line === 'BEGIN:VEVENT') {
            event = {}; // Start a new event object
        } else if (line === 'END:VEVENT') {
            if (event) {
                events.push(event); // Add completed event to the list
                event = null; // Reset for the next event
            }
        } else if (event) {
            // Split key and value, handling cases where value might contain colons
            let [key, ...valueParts] = line.split(':');
            let value = valueParts.join(':').trim();

            // Handle parameters (e.g., DTSTART;TZID=America/New_York)
            let keyParts = key.split(';');
            key = keyParts[0].trim(); // Get the main property name

            switch (key) {
                case 'SUMMARY':
                    event.summary = value;
                    break;
                case 'DTSTART':
                    // Store the raw ICS date string for later parsing
                    event.start = value;
                    break;
                case 'DTEND':
                    // Store the raw ICS date string
                    event.end = value;
                    break;
                case 'DURATION':
                    event.duration = value;
                    break;
                case 'DESCRIPTION':
                    // Unescape newlines
                    event.description = value.replace(/\\n/g, '\n');
                    break;
                case 'LOCATION':
                    // Unescape backslashes and replace escaped newlines with comma-space for display
                    event.location = value.replace(/\\/g, '').replace(/, /g, ', ');
                    break;
                case 'UID':
                    event.uid = value;
                    break;
                // Add more cases for other properties you need (e.g., STATUS, URL)
            }
        }
    });
    return events;
}

/**
 * Displays the generated Google Calendar links or a message for too many events.
 * @param {Array<object>} events An array of parsed event objects.
 */
function displayEvents(events) {
    const container = document.getElementById('eventLink');
    if (!container) {
        console.error('Element with ID "eventLink" not found.');
        return;
    }

    if (events.length === 0) {
        container.innerHTML = '<p>No events found in the ICS file.</p>';
    } else if (events.length > 5) {
        // Recommend manual import for many events
        container.innerHTML = `<p>There are ${events.length} events in this file. It is recommended to import this ICS file manually into your calendar.</p>`;
    } else {
        // Generate links for a manageable number of events
        let linksHtml = events.map(event => {
            const link = createGoogleCalendarLink(event);
            if (link === '#') { // Handle cases where link generation failed
                return `<p>Could not generate link for "${event.summary || 'Untitled Event'}".</p>`;
            }
            return `<a href="${link}" target="_blank" rel="noopener noreferrer">Add "${event.summary || 'Untitled Event'}" to Google Calendar</a><br>`;
        }).join('');
        container.innerHTML = linksHtml;
    }
}