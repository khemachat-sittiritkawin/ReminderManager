

import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class EditorController implements Initializable {

    public TextField titleTextField;
    public TextArea noteTextArea;
    public TextField timeTextField;
    public Button confirmButton;
    public TextField dateTextField;
    public Label invalidDateLabel;
    public Label invalidTimeLabel;

    private Stage stage;

    private Reminder reminder = new Reminder();

    private LocalDate dateInput;
    private LocalTime timeInput;

    private boolean edited = false;

    private boolean validDate = true;
    private boolean validTime = true;


    public boolean isInputValid() {
        return validDate && validTime;
    }

    public boolean isEdited() {
        return edited;
    }

    public Reminder getReminder() {
        return reminder;
    }


    public void setup(Stage stage) {
        this.stage = stage;
    }

    public Button getConfirmButton() {
        return confirmButton;
    }

    public void setReminder(Reminder reminder) {
        this.reminder = reminder;

        titleTextField.setText(reminder.getTitle());
        noteTextArea.setText(reminder.getNote());

        String dateString = reminder.getDeadline().format(ProgramConfig.getDateFormatter());
        String timeString = reminder.getDeadline().format(ProgramConfig.getTimeFormatter());

        dateTextField.setText(dateString);
        timeTextField.setText(timeString);

        dateInput = LocalDate.parse(dateString, ProgramConfig.getDateFormatter());
        timeInput = LocalTime.parse(timeString, ProgramConfig.getTimeFormatter());

        edited = false;
        validDate = true;
        validTime = true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setReminder(reminder);

        timeTextField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!edited) edited = true;
            LocalTime time = null;
            try {
                time = LocalTime.parse(newValue, ProgramConfig.getTimeFormatter());
            } catch (DateTimeParseException e) {
                invalidTimeLabel.setVisible(true);
                validTime = false;
            }
            if (time != null) {
                invalidTimeLabel.setVisible(false);
                timeInput = time;
                reminder.setDeadline(LocalDateTime.of(dateInput, timeInput));
                validTime = true;
            }
            confirmButton.setDisable(!isInputValid());
        });

        dateTextField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!edited) edited = true;
            LocalDate date = null;
            try {
                date = LocalDate.parse(newValue, ProgramConfig.getDateFormatter());
            } catch (DateTimeParseException e) {
                invalidDateLabel.setVisible(true);
                validDate = false;
            }
            if (date != null) {
                invalidDateLabel.setVisible(false);
                dateInput = date;
                reminder.setDeadline(LocalDateTime.of(dateInput, timeInput));
                validDate = true;
            }
            confirmButton.setDisable(!isInputValid());
        });


        titleTextField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            edited = true;
            reminder.setTitle(newValue);
        });

        noteTextArea.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            edited = true;
            reminder.setNote(newValue);
        });
    }
}
