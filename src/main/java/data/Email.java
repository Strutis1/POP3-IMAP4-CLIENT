package data;

import javafx.beans.property.*;

public class Email {
    private final IntegerProperty id;
    private final StringProperty company;
    private final StringProperty sender;
    private final StringProperty type;
    private final StringProperty subject;
    private final StringProperty size;

    private final String content;

    public Email(int id,String company, String sender, String type, String subject, String size, String content) {
        this.id = new SimpleIntegerProperty(id);
        this.company = new SimpleStringProperty(company);
        this.sender = new SimpleStringProperty(sender);
        this.type = new SimpleStringProperty(type);
        this.subject = new SimpleStringProperty(subject);
        this.size = new SimpleStringProperty(size);
        this.content = content;
    }

    public int getId() {
        return id.get();
    }

    public String getCompany() {
        return company.get();
    }

    public String getSender() {
        return sender.get();
    }

    public String getType() {return type.get(); }

    public String getSubject() {
        return subject.get();
    }

    public String getSize() {
        return size.get();
    }

    public String getContent() {
        return content;
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty companyProperty() {
        return company;
    }

    public StringProperty senderProperty() {
        return sender;
    }

    public StringProperty typeProperty() {return type; }

    public StringProperty subjectProperty() {
        return subject;
    }

    public StringProperty sizeProperty() {
        return size;
    }
}
