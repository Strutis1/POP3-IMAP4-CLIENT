package data;

import javafx.beans.property.*;

public class Email {
    private final IntegerProperty id;
    private final StringProperty sender;
    private final StringProperty subject;
    private final StringProperty size;

    public Email(int id, String sender, String subject, String size) {
        this.id = new SimpleIntegerProperty(id);
        this.sender = new SimpleStringProperty(sender);
        this.subject = new SimpleStringProperty(subject);
        this.size = new SimpleStringProperty(size);
    }

    public int getId() {
        return id.get();
    }

    public String getSender() {
        return sender.get();
    }

    public String getSubject() {
        return subject.get();
    }

    public String getSize() {
        return size.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty senderProperty() {
        return sender;
    }

    public StringProperty subjectProperty() {
        return subject;
    }

    public StringProperty sizeProperty() {
        return size;
    }
}
