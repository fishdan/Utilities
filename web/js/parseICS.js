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
    }
}

function parseICSData(icsData) {
    const events = [];
    let lines = icsData.split(/\r\n|\n|\r/);
    let event = null;

    lines.forEach(line => {
        if (line === 'BEGIN:VEVENT') {
            event = {};
        } else if (line === 'END:VEVENT') {
            events.push(event);
            event = null;
        } else if (event) {
            let [key, ...valueParts] = line.split(':');
            let value = valueParts.join(':').trim();
            let keyParts = key.split(';');
            key = keyParts[0].trim();

            switch (key) {
                case 'SUMMARY':
                    event.summary = value;
                    break;
                case 'DTSTART':
                    event.start = value;
                    break;
                case 'DTEND':
                    event.end = value;
                    break;
                case 'DESCRIPTION':
                    event.description = value.replace(/\\n/g, '\n');
                    break;
                case 'LOCATION':
                    event.location = value.replace(/\\/g, '').replace(/\\n/g, ', ');
                    break;
            }
        }
    });
    return events;
}

function displayEvents(events) {
    const container = document.getElementById('eventLink');
    if (events.length > 5) {
        container.innerHTML = `<p>There are ${events.length} events in this file. It is recommended to import this ICS file manually into your calendar.</p>`;
    } else {
        let links = events.map(event => {
            const link = createGoogleCalendarLink(event);
            return `<a href="${link}" target="_blank">Add "${event.summary}" to Google Calendar</a><br>`;
        }).join('');
        container.innerHTML = links;
    }
}

function createGoogleCalendarLink(event) {
    const baseUrl = 'https://calendar.google.com/calendar/r/eventedit';
    let params = `text=${encodeURIComponent(event.summary)}`;
    params += `&dates=${formatDateTime(event.start)}/${formatDateTime(event.end)}`;
    if (event.description) {
        params += `&details=${encodeURIComponent(event.description)}`;
    }
    if (event.location) {
        params += `&location=${encodeURIComponent(event.location)}`;
    }
    return `${baseUrl}?${params}`;
}

function formatDateTime(dt) {
    return dt.replace(/(\d{4})(\d{2})(\d{2})T(\d{2})(\d{2})(\d{2})/, '$1$2$3T$4$5$6Z');
}
