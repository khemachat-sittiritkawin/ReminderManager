import java.time.LocalDateTime;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Reminder implements Cloneable {
    private String title = "Untitled";
    private LocalDateTime deadline = LocalDateTime.now();
    private String note = "None";
    private transient BooleanProperty due = new SimpleBooleanProperty(false);

    @Override
    public Object clone() throws CloneNotSupportedException {
        Reminder reminder = (Reminder)super.clone();
        reminder.due = new SimpleBooleanProperty(false);
        return reminder;
    }

    public Reminder() {}

    public Reminder(String title, LocalDateTime due, String note) {
        this.title = title;
        this.deadline = due;
        this.note = note;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BooleanProperty dueProperty() {
        return due;
    }

    public boolean isDue() {
        return due.get();
    }

    public void setDue(boolean value) {
        due.set(value);
    }
}