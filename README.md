# OODP ReminderList Manager
-------------------------------------------
Member of group
-------------------------------------------

1.Chayabordin Maophimpha (6631503013)

2.Khemachat Sittiritkawin (6631503005)

3.Apichai dancharoenrak (6631503047)

4.Pokpong Romsri (6631503025)

5.Korrakit Lhakdee (6631503102)

-------------------------------------------


Controller

This JavaFX controller class, Controller, manages a simple reminder list application. It includes methods to handle adding, deleting, and editing reminders. Here's a brief breakdown:
The initialize method sets up the initial state of the UI components and binds them to the data model.
The updateSelectionDetails method updates the details displayed when a reminder is selected in the list.
The handleCreateButton, handleDeleteButton, and handleEditButton methods respond to user actions on corresponding buttons for creating, deleting, and editing reminders.
The handleCloseMenuItem method handles the action of closing the application window.
The class interacts with a Reminder class and a Database singleton class to manage reminders and their persistence. It also utilizes JavaFX components like buttons, labels, list views, and text areas for the user interface.


Database

This Database class manages the persistence of reminders using Google's Gson library for JSON serialization. Here's a summary of its key features:
It follows the Singleton design pattern, ensuring only one instance of the database exists throughout the application's lifecycle.
The init method initializes the database instance, while the shutdown method cleans up resources when the application exits.
It defines an inner UpdateThread class that periodically updates the status of reminders based on their deadlines.
The reminders field holds an observable list of Reminder objects, which are the reminders managed by the database.
The class utilizes Gson's JsonSerializer and JsonDeserializer to handle serialization and deserialization of LocalDateTime objects.
During initialization, it attempts to load reminders from a save file. If the file doesn't exist or is corrupted, appropriate messages are printed.
The cleanup method is responsible for saving reminders to the save file when the application shuts down.
During initialization, the class also creates a thread to keep track of the deadline property of Reminder objectss storing in the ObservableList object whether or not they are reached. If so, it will change their due property, and since these due property are of type BooleanProperty this will also trigger events indicating that these property have been edited.

EditorController

This EditorController class is responsible for managing the UI logic of the editor window, where users can create or edit reminders. Here's a summary of its functionality:
The class implements the Initializable interface, which allows it to initialize UI components when the FXML file is loaded.
It defines JavaFX components such as text fields, text areas, buttons, and labels for the user interface.
The setup method initializes the controller with the stage (window) it belongs to.
The setReminder method populates the editor fields with data from an existing reminder when the editor is used for editing.
The initialize method sets up event listeners for various UI components to respond to user input.
Event listeners are set up for text fields (titleTextField, dateTextField, timeTextField) and text area (noteTextArea) to capture changes made by the user.
When the user modifies the title or note, the corresponding properties of the reminder object are updated.
When the user modifies the date or time, the input is validated and stored in dateInput or timeInput, and the deadline property of the reminder object is updated accordingly.
The isInputValid method checks whether the user input for date and time is valid.
The isEdited method indicates whether any changes have been made in the editor window.
The getReminder method retrieves the reminder object after modifications have been made by the user.


EditorWindow

This EditorWindow class manages the editor window for creating or editing reminders. Here's a breakdown of its functionality:
It initializes the editor window by loading an FXML file (reminder-editor-ui.fxml) containing the UI layout and setting up the necessary event handlers.
The onConfirmButtonPressed method is invoked when the user clicks the confirm button in the editor window. If the input is valid, it sets the saveChanges flag to true and closes the window.
The handleCloseRequest method is triggered when the user attempts to close the window. If there are unsaved changes, it prompts the user with a confirmation dialog to save changes, discard them, or cancel the close operation.
The setReminder method sets the reminder to be edited in the editor window.
The savedChanges method returns a boolean indicating whether changes were saved before closing the editor window.
The getReminder method retrieves the reminder object after editing.
The run method displays the editor window and waits for it to be closed before returning.
Overall, the EditorWindow class encapsulates the functionality for managing the editor window's lifecycle, user interactions, and data manipulation.


Program

This Program class serves as the entry point for the JavaFX application. Here's what each method does:
The start method is called when the application is launched. It loads the main UI layout from the FXML file (main-ui.fxml) using a FXMLLoader, sets up the scene with the loaded layout, sets the stage title, and displays the stage.
The init method is called before the application starts and initializes the database. It calls the init method of the Database class to initialize the database instance.
The stop method is called when the application is shutting down and shuts down the database. It calls the shutdown method of the Database class to perform any necessary cleanup.
The main method is the entry point of the application. It launches the JavaFX application by calling the launch method inherited from the Application class.
Overall, the Program class coordinates the initialization, startup, and shutdown of the application and integrates with the main UI controller and the database.


ProgramConfig

This ProgramConfig class provides configuration settings for the program, such as file names and date/time format patterns. Here's a breakdown of its functionality:
It defines constants for the save file name (SAVE_FILE_NAME) and date/time format patterns (DATE_FORMAT_PATTERN and TIME_FORMAT_PATTERN).
It provides static methods to retrieve DateTimeFormatter objects configured with the specified date and time format patterns.
The getDateFormatter method returns a DateTimeFormatter configured with the date format pattern.
The getTimeFormatter method returns a DateTimeFormatter configured with the time format pattern.
The getDateTimeFormatter method returns a DateTimeFormatter configured with both the date and time format patterns concatenated together.
Overall, the ProgramConfig class centralizes configuration settings related to date/time formatting, promoting consistency and ease of maintenance throughout the program.


Reminder

This Reminder class represents a reminder object with properties like title, deadline, and note. Here's a summary of its features:
It implements the Cloneable interface, allowing instances of Reminder to be cloned.
It defines properties for the title, deadline (as a LocalDateTime object), and note.
It includes a transient(meaning this property will be excluded when serializing/deserializing an object) BooleanProperty named due, which represents whether the reminder is due. This property also makes use of BooleanProperty so that any class that uses this class can listen to an event indicating a change made in its value(see the Database class section for more detail).
The clone method overrides the clone method of the Object class to provide a shallow copy(the BooleanProperty field is excluded to remove any event listener connecting to the copied object beforehand) of the Reminder object.
The class provides constructors to create reminder objects with or without initial values for title, deadline, and note.
Getter and setter methods are provided for accessing and modifying the properties of the reminder.
The dueProperty, isDue, and setDue methods are used to work with the due property as a JavaFX BooleanProperty.
Overall, the Reminder class encapsulates the data and behavior of a reminder item, making it easy to work with reminder objects within the application.

ReminderListCell

This ReminderListCell class customizes the appearance of each item in a JavaFX ListView that displays reminders. Here's a breakdown of its functionality:
It extends the ListCell class and overrides its updateItem method to customize the appearance of the cell.
Inside the updateItem method, it first calls the superclass method super.updateItem(item, empty) to handle the default behavior.
If the item is empty or null, it sets the text and graphic of the cell to null.
If the item is not empty, it creates labels for the title, date, and time of the reminder.
It updates the appearance of the labels based on the due property of the reminder. If the reminder is due(due property is true. For more detail, see the section for the Reminder class and Database class), the text color is set to red; otherwise, it's set to black.
It adds a listener to the due property of the reminder to update the appearance of the labels whenever the due property changes.
It creates an HBox layout to hold the labels (title, date, and time) and sets the spacing between them.
Finally, it sets the graphic of the cell to the HBox layout, effectively customizing the appearance of the cell.
Overall, the ReminderListCell class allows for customizing the visual representation of reminder items in a ListView, making it easy to distinguish between due and non-due reminders.

*Note: We originally intended to make this class an anonymous class that got passed onto the Controller class' setCellFactory method as the argument, but we made the anonymous class into a separate class to increase customizability and readability.
