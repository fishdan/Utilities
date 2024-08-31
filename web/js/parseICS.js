function parseICS() {
    const fileInput = document.getElementById('fileInput');
    if (fileInput.files.length > 0) {
        const reader = new FileReader();
        reader.onload = function(e) {
            const contents = e.target.result;
            const event = parseICSData(contents);
            if (event) {
                const googleCalLink = createGoogleCalendarLink(event);
                const linkHtml = `<a href="${googleCalLink}" target="_blank">Add to Google Calendar</a>`;
                document.getElementById('eventLink').innerHTML = linkHtml;
            }
        };
        reader.readAsText(fileInput.files[0]);
    }
}

function parseICSData(icsData) {
    const lines = icsData.split(/\r\n|\n|\r/);
    let event = {};
    lines.forEach(line => {
        // Extract the key and value, ignoring any parameters like language
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
                event.description = value.replace(/\\n/g, '\n'); // Handling escaped newlines
                break;
            case 'LOCATION':
                // Remove backslashes and handle escaped newlines if present
                event.location = value.replace(/\\/g, '').replace(/\\n/g, ', ');
                break;
        }
    });
    return event;
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
    // Format should be: YYYYMMDDTHHmmssZ for UTC time.
    // Here we assume the ICS provides UTC time; adjust parsing as needed.
    return dt.replace(/(\d{4})(\d{2})(\d{2})T(\d{2})(\d{2})(\d{2})/, '$1$2$3T$4$5$6Z');
}
