
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Program extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Program.class.getResource("main-ui.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 829, 477);
        Controller controller = fxmlLoader.getController();
        controller.setStage(stage);
        stage.setTitle("Reminder Manager");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void init() throws Exception {
        Database.init();
    }

    @Override
    public void stop() throws Exception {
        Database.shutdown();
    }

    public static void main(String[] args) {
        launch();
    }


}