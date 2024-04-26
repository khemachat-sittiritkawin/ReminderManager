import javafx.beans.value.ChangeListener;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class ReminderListCell extends ListCell<Reminder> {

    Label titleLabel;
    Label dateLabel;
    Label timeLabel;

    ChangeListener<Boolean> duePropertyListener = (observable, oldValue, newValue) -> updateDue(newValue);

    Reminder currentItem;

    private void updateDue(Boolean due) {
        if (!due) {
            titleLabel.setTextFill(Color.BLACK);
            dateLabel.setTextFill(Color.BLACK);
            timeLabel.setTextFill(Color.BLACK);

        } else {
            titleLabel.setTextFill(Color.RED);
            dateLabel.setTextFill(Color.RED);
            timeLabel.setTextFill(Color.RED);
        }
    }

    @Override
    protected void updateItem(Reminder item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (currentItem == null) {
                currentItem = item;
                currentItem.dueProperty().addListener(duePropertyListener);
            } else if (currentItem != item) {
                currentItem.dueProperty().removeListener(duePropertyListener);
                item.dueProperty().addListener(duePropertyListener);
            }
            currentItem = item;

            titleLabel = new Label(item.getTitle());
            dateLabel = new Label(ProgramConfig.getDateFormatter().format(item.getDeadline()));
            timeLabel = new Label(ProgramConfig.getTimeFormatter().format(item.getDeadline()));

            updateDue(item.isDue());

            HBox hbox = new HBox(titleLabel, dateLabel, timeLabel);
            hbox.setSpacing(30);
            setGraphic(hbox);
        }
    }
}