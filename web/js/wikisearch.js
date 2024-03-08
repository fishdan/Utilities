document.addEventListener('DOMContentLoaded', function() {
    // Finds all <a> tags containing 'wikisearch'
    const links = document.querySelectorAll('a[href*="wikisearch"]');
    let counter = 1; // Initialize counter for ID increment

    links.forEach(link => {
        // Constructs the Wikipedia search URL using the inner text of the link
        var searchText = link.innerText;
        var wikiUrl = 'https://en.wikipedia.org/wiki/' + encodeURIComponent(searchText);

        // Creates a new span element
        const span = document.createElement('span');
        span.className = 'wiki-search'; // Adds the 'wiki-search' class to the span
        span.id = 'wikisearch' + counter++; // Assigns incremented ID to each span
        span.textContent = link.textContent; // Sets the span's text content to match that of the link

        // Adds an event listener to redirect to the Wikipedia URL when the span is clicked
        span.addEventListener('click', function() {
            window.location.href = wikiUrl;
        });

        // Replaces the <a> tag with the new span in the DOM
        link.parentNode.replaceChild(span, link);
    });
});
