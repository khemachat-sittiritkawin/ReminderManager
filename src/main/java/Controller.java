import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public Button removeButton;

    @FXML
    public Button addButton;

    @FXML
    public Button editButton;

    @FXML
    public ListView<Reminder> reminderListView;

    @FXML
    public Label itemCountLabel;

    @FXML
    public Label selectedTitleLabel;
    @FXML
    public Label selectedDueLabel;

    @FXML
    public TextArea selectedNoteTextArea;
    public Label noItemSelectedLabel;
    public VBox detailVBox;
    public MenuItem closeMenuItem;


    Stage stage;

    private void updateSelectionDetails(Reminder item) {
        if (item != null) {
            detailVBox.setVisible(true);
            noItemSelectedLabel.setVisible(false);
            selectedTitleLabel.setText(item.getTitle());
            DateTimeFormatter dateTimeFormatter = ProgramConfig.getDateTimeFormatter();
            selectedDueLabel.setText(dateTimeFormatter.format(item.getDeadline()));
            selectedNoteTextArea.setText(item.getNote());
            selectedTitleLabel.setText(item.getTitle());
        } else {
            detailVBox.setVisible(false);
            noItemSelectedLabel.setVisible(true);
            selectedTitleLabel.setText("N/A");
            selectedDueLabel.setText("N/A");
            selectedNoteTextArea.setText("N/A");
            selectedTitleLabel.setText("N/A");
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateSelectionDetails(null);
        ObservableList<Reminder> reminders = Database.getInstance().getReminders();
        reminders.addListener((ListChangeListener<Reminder>) c -> itemCountLabel.setText(reminders.size() + " item(s)"));

        itemCountLabel.setText(reminders.size() + " item(s)");
        SortedList<Reminder> sortedList = new SortedList<>(Database.getInstance().getReminders(), (o1, o2) -> o1.getDeadline().compareTo(o2.getDeadline()));
        reminderListView.setItems(sortedList);
        reminderListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Reminder selection = reminderListView.getSelectionModel().getSelectedItem();
            updateSelectionDetails(selection);
        });

        reminderListView.setCellFactory(param -> new ReminderListCell());
    }


    @FXML
    public void handleCreateButton() throws IOException {
        EditorWindow editor = new EditorWindow();
        editor.run();
        if (editor.savedChanges()) {
            Database.getInstance().getReminders().add(editor.getReminder());
        }
    }

    @FXML
    public void handleDeleteButton() {
        Reminder selected = reminderListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Database.getInstance().getReminders().remove(selected);
        }
    }

    @FXML
    public void handleEditButton() throws IOException, CloneNotSupportedException {
        Reminder selected = reminderListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        EditorWindow editor = new EditorWindow();
        editor.setReminder(selected);
        editor.run();
        if (editor.savedChanges()) {
            Reminder newReminder = editor.getReminder();
            Database.getInstance().getReminders().remove(selected);
            Database.getInstance().getReminders().add(newReminder);
        }
    }

    public void handleCloseMenuItem(ActionEvent actionEvent) {
        stage.close();
    }
}