import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Optional;

public class EditorWindow {
    private Stage stage;
    private EditorController controller;
    private boolean saveChanges = false;

    public EditorWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("reminder-editor-ui.fxml"));
        Scene scene = new Scene(loader.load());
        this.stage = new Stage();
        stage.setScene(scene);

        controller = loader.getController();
        controller.setup(stage);
        controller.getConfirmButton().setOnAction(this::onConfirmButtonPressed);
        stage.setOnCloseRequest(this::handleCloseRequest);
    }

    private void onConfirmButtonPressed(ActionEvent actionEvent) {
        if (controller.isInputValid()) {
            saveChanges = true;
            stage.close();
        }
    }

    private void handleCloseRequest(WindowEvent event) {
        if (!controller.isEdited()) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText("You have unsaved changes. Do you wish to save them?");

        ButtonType yesButtonType = new ButtonType("Yes");
        ButtonType noButtonType = new ButtonType("No");
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        if (controller.isInputValid()) {
            alert.getButtonTypes().setAll(yesButtonType, noButtonType, cancelButtonType);
        } else {
            alert.getButtonTypes().setAll(noButtonType, cancelButtonType);
        }

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == cancelButtonType) {
            System.out.println("Canceled");
            event.consume();
        } else if (result.get() == noButtonType) {
            saveChanges = false;
        } else if (result.get() == yesButtonType) {
            saveChanges = true;
        }

    }

    public void setReminder(Reminder reminder) throws CloneNotSupportedException {
        controller.setReminder((Reminder)reminder.clone());
    }

    public boolean savedChanges() {
        return saveChanges;
    }

    public Reminder getReminder() {
        return controller.getReminder();
    }

    public Stage getStage() {
        return stage;
    }

    public EditorController getController() {
        return controller;
    }

    public void run() {
        stage.showAndWait();
    }
}
