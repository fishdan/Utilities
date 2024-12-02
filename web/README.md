### ICS to Google Calendar Utility

**Description**:  
This tool simplifies the process of adding events from an ICS (iCalendar) file to your Google Calendar. ICS files are commonly used for sharing calendar events across platforms, but importing them into Google Calendar can be cumbersome. With this utility, users can upload their ICS file and instantly generate a link to add the event directly to Google Calendar, making the process seamless and efficient.

**Features**:
- Upload an ICS file from your computer.
- Generate a direct link to add the event to Google Calendar.
- Eliminates the multi-step import process, offering a user-friendly experience.
- Mobile-friendly for on-the-go use.

**Reference Implementation**:  
[ICS to Google Calendar Utility](https://www.cs.umb.edu/~dfish/ICS2Google.html)

---

### Centered Circle or Square on Google Maps

**Description**:  
This utility displays a Google Map and allows users to visualize either a centered circle or square over a geographic area. The size of the shape dynamically adjusts to the map's viewport dimensions, making it an effective tool for analyzing proximity or planning geographically bounded activities.

**Features**:
- Displays a map centered at specified coordinates.
- Option to switch between a circle or square overlay on the map.
- Shape size dynamically scales based on the map's viewport.
- Interactive and responsive to map zoom and movement.

**Use Cases**:
- Visualizing radius-based zones (e.g., delivery areas, event boundaries).
- Mapping square or rectangular regions for analysis.

**Reference Implementation**:  
[Centered Circle or Square on Google Maps](https://www.cs.umb.edu/~dfish/circleMap.html)

---

### Hangman Game (Dewordle)

**Description**:  
This is a dynamic Hangman-style word guessing game where users attempt to guess a hidden word, one letter at a time. The word is encoded in the URL as a Base64 parameter, providing a customizable experience. The game provides visual feedback for each guess, highlighting correct letters in green and partially correct letters (present but in the wrong position) in yellow.

**Features**:
- Dynamically generated input fields based on the length of the hidden word.
- Visual feedback for correct and partially correct guesses.
- Customizable word input via a Base64-encoded URL parameter.
- Encourages iterative guessing, adding new input rows for each incorrect attempt.

**How to Use**:
1. Provide the word to guess as a Base64-encoded string in the `word` URL parameter (e.g., `?word=encodedString`).
2. Players enter guesses letter by letter, receiving visual cues for correctness.
3. The game continues until the word is correctly guessed.

**Reference Implementation**:  
[Hangman Game (Dewordle)](https://www.cs.umb.edu/~dfish/dewordle.html)


---

### Echo Parameter Display

**Description**:  
This utility demonstrates a simple way to display a repeated message based on a URL parameter. By appending the `echo` parameter to the URL, users can generate a list of 10 repeated instances of the provided value, styled in a clean and minimalistic layout.

**Features**:
- Dynamically reads and decodes the `echo` URL parameter.
- Displays the value of the `echo` parameter 10 times in a styled list format.
- Ideal for testing URL parameter handling or generating simple repeated content.

**How to Use**:
1. Add the `echo` parameter to the URL with the desired text (e.g., `?echo=YourMessage`).
2. The page will dynamically create a list showing the provided message 10 times.

**Reference Implementation**:  
[Echo Parameter Display](https://www.cs.umb.edu/~dfish/echo.html)

---

### Fuel Savings Calculator

**Description**:  
This utility estimates the weekly cost savings when switching to a more fuel-efficient vehicle. By inputting your current gas price, weekly mileage, and the fuel efficiencies (miles per gallon) of your old and new vehicles, the tool calculates the potential savings on fuel costs.

**Features**:
- Input fields for gas price, weekly mileage, and vehicle fuel efficiencies.
- Calculates the weekly cost savings based on fuel consumption differences.
- Provides a direct link to learn about fuel-efficient car tax incentives at [FuelEconomy.gov](https://www.fueleconomy.gov/).

**How to Use**:
1. Enter your gas price per gallon.
2. Enter your average weekly miles driven.
3. Provide the gas mileage (MPG) of your old and new cars.
4. Click "Calculate Savings" to view your estimated weekly savings.

**Reference Implementation**:  
[Fuel Savings Calculator](https://www.cs.umb.edu/~dfish/gasCalculator.html)

---

### Medication Reminder App

**Description**:  
This utility helps users manage their medication schedules by generating personalized reminder events. By inputting the name of the medication, the first dose time, dosing interval, and total duration, the tool creates an iCalendar file containing all reminder events, complete with alerts 10 minutes before each dose. Users can import the file into their favorite calendar app to receive notifications and ensure strict adherence to their medication schedule.

**Features**:
- Input fields for event name, first occurrence, interval (hours), and total duration (days).
- Automatically generates reminders spaced at the specified interval over the given duration.
- Includes alerts 10 minutes before each dose.
- Downloads an iCalendar (.ics) file for easy import into calendar applications.

**Use Cases**:
- Managing medication schedules.
- Creating recurring event reminders for any time-sensitive tasks.

**How to Use**:
1. Enter the medication name (or event name).
2. Specify the first occurrence time and date.
3. Set the interval in hours between doses.
4. Provide the total duration in days for the reminders.
5. Click "Create Reminder" to generate and download the .ics file.

**Reference Implementation**:  
[Medication Reminder App](https://www.cs.umb.edu/~dfish/hourlyReminder.html)

---

### Hangman Game Instructions and Game Link Generator

**Description**:  
This utility provides instructions for playing the Hangman game and allows users to generate a unique game link based on a custom word. By entering a word into the input field, users can create a sharable link to play a personalized Hangman game where the entered word serves as the target for guessing.

**Features**:
- Clear instructions for how to play the Hangman game.
- Simple input field for entering a custom word to generate a game link.
- Encodes the custom word into a Base64 format for secure sharing.
- Provides a clickable link to start a Hangman game with the custom word.

**How to Use**:
1. Review the instructions to understand the gameplay mechanics.
2. Enter a custom word into the input field.
3. Click "Generate Game Link" to create a unique link for the Hangman game.
4. Share the link or use it to start a new game.

**Reference Implementation**:  
[Hangman Game Instructions and Link Generator](https://www.cs.umb.edu/~dfish/prewordle.html)

---

### URL to QR Code Generator

**Description**:  
This utility allows users to easily generate a QR code from any URL. With a simple input field and a "Generate QR Code" button, the tool creates a scannable QR code for the provided URL. The generated QR code can be saved locally for sharing or use in various applications. A tipping option is also available for those who wish to support the service.

**Features**:
- Input field for entering any valid URL.
- Generates a QR code instantly for the provided URL.
- Allows users to save the QR code image locally.
- Includes a tipping feature for optional contributions.

**How to Use**:
1. Enter a valid URL into the input field.
2. Press "Enter" or click the "Generate QR Code" button to create a QR code.
3. Right-click on the generated QR code and select "Save Image As" to download it.
4. Optionally, use the provided PayPal link to leave a tip.

**Reference Implementation**:  
[URL to QR Code Generator](https://www.cs.umb.edu/~dfish/qrcode.html)

---

### Quad Video Player for YouTube

**Description**:  
This utility showcases a responsive layout for displaying four embedded YouTube videos simultaneously. The tool uses a flexible container to ensure the videos are neatly arranged and adapt to different screen sizes. Videos are embedded with autoplay and mute options enabled, providing a seamless viewing experience.

**Features**:
- Displays up to four YouTube videos simultaneously in a responsive grid layout.
- Ensures videos maintain a 16:9 aspect ratio.
- Videos autoplay with mute enabled for a smooth, uninterrupted experience.
- Suitable for creating curated video playlists or showcasing multiple videos.

**How to Use**:
1. Open the page to view four embedded YouTube videos arranged in a grid.
2. Adjust the browser window size to see the responsive behavior.
3. Use the embedded video controls to manage playback or switch to fullscreen.

**Reference Implementation**:  
[Quad Video Player for YouTube](https://www.cs.umb.edu/~dfish/quadvideo.html)

---

### Reverse URL Tool

**Description**:  
The Reverse URL Tool takes a user-provided URL and reverses its characters. If the reversed URL starts with "http", the tool automatically redirects to the reversed URL. Otherwise, it displays the reversed URL as plain text on the page. This tool is useful for exploring how URLs are transformed or for fun with URL manipulation.

**Features**:
- Reverses the characters of any input URL.
- Automatically redirects if the reversed URL starts with "http".
- Displays the reversed URL on the page if it doesn't start with "http".
- Includes a reset button to clear inputs and outputs.

**How to Use**:
1. Enter a URL into the input field.
2. Click "Reverse & Check" to reverse the URL.
3. If the reversed URL begins with "http", you will be redirected to it; otherwise, it will be displayed below the input.
4. Use the "Reset" button to clear the input and try a new URL.

**Reference Implementation**:  
[Reverse URL Tool](https://www.cs.umb.edu/~dfish/reverseURL.html)

Here’s the README section for this utility:

---

### Dynamic Wikipedia Search Links

**Description**:  
This utility demonstrates how to dynamically convert specific hyperlinks into interactive Wikipedia search links. Designed for use in content management systems (CMS) like WordPress, it automates the process of linking text to Wikipedia search results. By appending the script and styles to your platform, links with a placeholder keyword (e.g., "wikisearch") are transformed into functional Wikipedia search links, simplifying content enrichment and enhancing user engagement.

**Features**:
- Automatically transforms placeholder links into direct Wikipedia search links.
- Saves time by eliminating the need to manually search and link to Wikipedia articles.
- Integrates seamlessly with CMS platforms, such as WordPress.
- Includes a demo modal showcasing the functionality with an animated preview.

**How to Use**:
1. Add links with the keyword `wikisearch` in the `href` attribute.
2. Include the provided JavaScript and CSS files in your website or CMS theme.
3. On page load, these links will be converted into direct Wikipedia search links based on their text.
4. Use the GitHub link provided on the page to access the source code for integration.

**Reference Implementation**:  
[Dynamic Wikipedia Search Links](https://www.cs.umb.edu/~dfish/wikisearch.html)

---
